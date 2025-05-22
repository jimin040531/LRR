package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.common.ReserveResult;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReserveManager {
    // 사용자 정보 파일 경로 (예약 정보도 이 파일에 저장)
    private static final String USER_FILE = receiveController.getFilepath() + receiveController.getFileName();
    // src/main/resources/UserInfo.txt
    private static final int MAX_RESERVE = 4; // 최대 예약 개수

    // 5번: 파일 접근 동기화용 락 객체 추가
    private static final Object FILE_LOCK = new Object();

    /**
     * 예약 요청을 처리하는 메서드입니다.
     * 
     * @param id         사용자 ID (UserInfo.txt의 3번째 필드)
     * @param role       사용자 역할(학생/교수) (UserInfo.txt의 1번째 필드)
     * @param roomNumber 강의실 번호
     * @param date       예약 날짜(년 월 일 시작시간(시:분) 끝시간(시:분)), 예시 "2025 / 05 / 21 / 12:00
     *                   13:00"
     * @param day        예약 요일
     * @return ReserveResult(예약 성공/실패 및 사유)
     */
    public static ReserveResult reserve(String id, String role, String roomNumber, String date, String day) {
        synchronized (FILE_LOCK) {
            // 예약 날짜/시간이 과거인지 체크하고 예약 단위(50분 또는 1시간) 체크
            try {
                // date 예시: "2025 / 05 / 21 / 12:00 13:00"
                String[] dateParts = date.split("/");
                if (dateParts.length != 4) {
                    return new ReserveResult(false, "예약 날짜/시간 형식이 올바르지 않습니다. (예: 2025 / 05 / 21 / 12:00 13:00)");
                }
                String year = dateParts[0].trim();
                String month = dateParts[1].trim();
                String dayOfMonth = dateParts[2].trim();
                String[] times = dateParts[3].trim().split(" ");
                if (times.length != 2) {
                    return new ReserveResult(false, "예약 시간 정보가 올바르지 않습니다. (예: 12:00 13:00)");
                }
                String startTime = times[0];
                String endTime = times[1];

                // 날짜와 시간 형식이 올바른지 추가로 체크
                LocalDateTime startDateTime = LocalDateTime.parse(
                        year + "-" + month + "-" + dayOfMonth + "T" + startTime,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                LocalDateTime endDateTime = LocalDateTime.parse(
                        year + "-" + month + "-" + dayOfMonth + "T" + endTime,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

                // 과거 예약 불가
                if (startDateTime.isBefore(LocalDateTime.now())) {
                    return new ReserveResult(false, "과거 시간대에는 예약할 수 없습니다.");
                }

                // 1시간 또는 50분 단위만 허용
                long minutes = java.time.Duration.between(startDateTime, endDateTime).toMinutes();
                if (!(minutes == 50 || minutes == 60)) {
                    return new ReserveResult(false, "예약은 50분 또는 1시간 단위로만 가능합니다.");
                }
            } catch (Exception e) {
                return new ReserveResult(false, "날짜/시간 형식 오류");
            }

            List<String> lines = new ArrayList<>(); // 파일 전체 라인 저장
            boolean updated = false; // 예약 정보 갱신 여부
            String newReserve = makeReserveInfo(roomNumber, date, day); // 예약 정보 문자열

            // 한 시간대 40명 초과 예약 제한
            int userCount = countUsersByReserveInfo(newReserve);
            if (userCount >= 40) {
                return new ReserveResult(false, "동일 시간대 최대 예약 인원(40명) 초과");
            }

            // 교수 예약 시, 다른 교수의 동일 예약 중복 체크
            if ("P".equals(role)) {
                try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        // UserInfo.txt: 역할,이름,ID,비번,예약1,예약2,...
                        if (parts.length >= 4 && "P".equals(parts[0].trim()) && !parts[2].trim().equals(id)) {
                            for (int i = 4; i < parts.length; i++) {
                                if (equalsReserveInfo(parts[i], newReserve)) {
                                    return new ReserveResult(false, "이미 다른 교수가 해당 시간에 예약했습니다.");
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ReserveResult(false, "파일 읽기 오류");
                }
            }

            // ...기존 예약 처리(학생/교수 공통)...
            try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    // UserInfo.txt: 역할,이름,ID,비번,예약1,예약2,...
                    if (parts.length >= 4 && parts[2].trim().equals(id)) {
                        List<String> reserves = new ArrayList<>();
                        for (int i = 4; i < parts.length; i++)
                            reserves.add(parts[i].trim());
                        for (String r : reserves) {
                            if (equalsReserveInfo(r, newReserve)) {
                                return new ReserveResult(false, "이미 동일한 예약이 존재합니다.");
                            }
                        }
                        if (reserves.size() >= MAX_RESERVE && !"P".equals(role)) {
                            return new ReserveResult(false, "최대 예약 개수 초과");
                        }
                        reserves.add(newReserve);
                        // 라인 재구성: 역할,이름,ID,비번,예약1,예약2,...
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 4; i++)
                            sb.append(parts[i]).append(i < 3 ? "," : "");
                        for (String r : reserves)
                            sb.append(",").append(r);
                        lines.add(sb.toString());
                        updated = true;
                    } else {
                        lines.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new ReserveResult(false, "파일 읽기 오류");
            }

            // 파일에 갱신
            if (updated) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
                    for (String l : lines) {
                        bw.write(l);
                        bw.newLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ReserveResult(false, "파일 저장 오류");
                }
                return new ReserveResult(true, "예약 성공");
            }
            return new ReserveResult(false, "사용자 정보를 찾을 수 없습니다.");
        }
    }

    // 예약 정보 생성(포맷 일관성 보장)
    public static String makeReserveInfo(String roomNumber, String date, String day) {
        return String.format("%s / %s / %s", roomNumber.trim(), date.trim(), day.trim());
    }

    // 예약 정보 비교(공백, 대소문자 무시)
    public static boolean equalsReserveInfo(String info1, String info2) {
        return info1.replaceAll("\\s+", " ").trim().equalsIgnoreCase(info2.replaceAll("\\s+", " ").trim());
    }

    /**
     * id로 예약 정보 조회 - 클라이언트 요청 시 사용
     * 
     * @param id 사용자 ID (UserInfo.txt의 3번째 필드)
     * @return 예약 정보 리스트
     */
    public static List<String> getReserveInfoById(String id) {
        synchronized (FILE_LOCK) {
            List<String> reserves = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    // UserInfo.txt: 역할,이름,ID,비번,예약1,예약2,...
                    if (parts.length >= 4 && parts[2].trim().equals(id)) {
                        for (int i = 4; i < parts.length; i++)
                            reserves.add(parts[i].trim());
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return reserves;
        }
    }

    /**
     * 6번: 예약 정보로 예약한 사용자 id 목록 조회
     * 
     * @param reserveInfo 예약 정보 문자열
     * @return 예약자 id 리스트
     */
    public static List<String> getUserIdsByReserveInfo(String reserveInfo) {
        synchronized (FILE_LOCK) {
            List<String> userIds = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String userId = parts[2].trim();
                        for (int i = 4; i < parts.length; i++) {
                            if (equalsReserveInfo(parts[i], reserveInfo)) {
                                userIds.add(userId);
                                break;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return userIds;
        }
    }

    /**
     * 예약 정보로 총 예약자 수 조회 - 클라이언트 요청 시 사용
     * 
     * @param reserveInfo 예약 정보 문자열
     * @return 예약자 수
     */
    public static int countUsersByReserveInfo(String reserveInfo) {
        synchronized (FILE_LOCK) {
            int count = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    // UserInfo.txt: 역할,이름,ID,비번,예약1,예약2,...
                    for (int i = 4; i < parts.length; i++) {
                        if (equalsReserveInfo(parts[i], reserveInfo)) {
                            count++;
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return count;
        }
    }

    // 기타 예약 관련 메서드(취소, 교수예약여부 등)도 동일하게 synchronized(FILE_LOCK) 적용
    public static ReserveResult cancelReserve(String id, String reserveInfo) {
        synchronized (FILE_LOCK) {
            List<String> lines = new ArrayList<>();
            boolean updated = false;

            try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4 && parts[2].trim().equals(id)) {
                        List<String> reserves = new ArrayList<>();
                        for (int i = 4; i < parts.length; i++)
                            reserves.add(parts[i].trim());
                        if (!reserves.removeIf(r -> equalsReserveInfo(r, reserveInfo))) {
                            return new ReserveResult(false, "해당 예약 정보를 찾을 수 없습니다.");
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 4; i++)
                            sb.append(parts[i]).append(i < 3 ? "," : "");
                        for (String r : reserves)
                            sb.append(",").append(r);
                        lines.add(sb.toString());
                        updated = true;
                    } else {
                        lines.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new ReserveResult(false, "파일 읽기 오류");
            }

            if (updated) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
                    for (String l : lines) {
                        bw.write(l);
                        bw.newLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ReserveResult(false, "파일 저장 오류");
                }
                return new ReserveResult(true, "예약이 취소되었습니다.");
            }
            return new ReserveResult(false, "사용자 정보를 찾을 수 없습니다.");
        }
    }

    public static boolean hasProfessorReserve(String reserveInfo) {
        synchronized (FILE_LOCK) {
            try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4 && parts[0].trim().equalsIgnoreCase("P")) {
                        for (int i = 4; i < parts.length; i++) {
                            if (equalsReserveInfo(parts[i], reserveInfo)) {
                                return true;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    // 교수 예약 시 학생 예약 중복 취소 및 해당 학생 ID 리스트 반환
    public static List<String> cancelStudentReservesForProfessor(String roomNumber, String date, String day) {
        synchronized (FILE_LOCK) {
            List<String> affectedStudentIds = new ArrayList<>();
            String targetReserve = makeReserveInfo(roomNumber, date, day);
            List<String> lines = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5 && "S".equals(parts[0].trim())) {
                        List<String> reserves = new ArrayList<>();
                        boolean removed = false;
                        for (int i = 4; i < parts.length; i++) {
                            if (equalsReserveInfo(parts[i], targetReserve)) {
                                removed = true;
                            } else {
                                reserves.add(parts[i].trim());
                            }
                        }
                        if (removed) {
                            affectedStudentIds.add(parts[2].trim());
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 4; i++)
                            sb.append(parts[i]).append(i < 3 ? "," : "");
                        for (String r : reserves)
                            sb.append(",").append(r);
                        lines.add(sb.toString());
                    } else {
                        lines.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return affectedStudentIds;
        }
    }
}