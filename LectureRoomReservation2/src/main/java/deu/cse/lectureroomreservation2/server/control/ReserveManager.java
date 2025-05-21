package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.common.ReserveResult;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReserveManager {
    // 사용자 정보 파일 경로 (예약 정보도 이 파일에 저장)
    private static final String USER_FILE = receiveController.getFilepath() + receiveController.getFileName();
    // src/main/resources/UserInfotest.txt
    private static final int MAX_RESERVE = 4; // 최대 예약 개수

    /**
     * 예약 요청을 처리하는 메서드입니다.
     * 
     * @param id         사용자 ID
     * @param role       사용자 역할(학생/교수)
     * @param roomNumber 강의실 번호
     * @param date       예약 날짜(년 월 일 시작시간(시:분) 끝시간(시:분)), 예시 "2025 / 05 / 21 / 12:00
     *                   13:00"
     * @param day        예약 요일
     * @return ReserveResult(예약 성공/실패 및 사유)
     */
    public static ReserveResult reserve(String id, String role, String roomNumber, String date, String day) {
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
                return new ReserveResult(false, "시작/끝 시간이 올바르지 않습니다. (예: 12:00 13:00)");
            }
            String startTime = times[0];
            String endTime = times[1];

            // 날짜와 시간 형식이 올바른지 추가로 체크
            try {
                LocalDateTime startDateTime = LocalDateTime.parse(
                        year + "-" + month + "-" + dayOfMonth + "T" + startTime,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                LocalDateTime endDateTime = LocalDateTime.parse(
                        year + "-" + month + "-" + dayOfMonth + "T" + endTime,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

                // 1. 과거 예약 불가
                if (startDateTime.isBefore(LocalDateTime.now())) {
                    return new ReserveResult(false, "과거 시간대에는 예약할 수 없습니다.");
                }

                // 2. 1시간 또는 50분 단위만 허용
                long minutes = java.time.Duration.between(startDateTime, endDateTime).toMinutes();
                if (!(minutes == 50 || minutes == 60)) {
                    return new ReserveResult(false, "예약은 50분 또는 1시간 단위로만 가능합니다.");
                }
            } catch (Exception e) {
                return new ReserveResult(false, "예약 날짜/시간 정보가 올바르지 않습니다. (예: 2025 / 05 / 21 / 12:00 13:00)");
            }
        } catch (Exception e) {
            return new ReserveResult(false, "예약 날짜/시간 정보가 올바르지 않습니다.");
        }

        List<String> lines = new ArrayList<>(); // 파일 전체 라인 저장
        boolean updated = false; // 예약 정보 갱신 여부
        String newReserve = roomNumber + " / " + date + " / " + day; // 예약 정보 문자열

        // 한 시간대 40명 초과 예약 제한
        int userCount = countUsersByReserveInfo(newReserve);
        if (userCount >= 40) {
            return new ReserveResult(false, "해당 시간대 예약 인원이 40명을 초과하여 예약할 수 없습니다.");
        }

        // 학생 예약 시 교수의 예약과 중복되는지 먼저 체크
        if ("S".equals(role)) {
            try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    // 교수(P)인 사용자만 검사
                    if (parts.length >= 6 && "P".equals(parts[4])) {
                        for (int i = 5; i < parts.length; i++) {
                            if (parts[i].trim().equals(newReserve)) {
                                // 이미 교수 예약이 있음
                                return new ReserveResult(false, "해당 시간/강의실은 교수에 의해 이미 예약되어 있습니다.");
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new ReserveResult(false, "파일 읽기 오류");
            }
        }

        // (학생/교수 예약 처리)..
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // 해당 id의 사용자를 찾으면 예약 정보 처리
                if (parts.length >= 5 && parts[0].equals(id)) {
                    List<String> reserves = new ArrayList<>();
                    for (int i = 5; i < parts.length; i++)
                        reserves.add(parts[i]);
                    // 이미 예약된 정보가 존재하면 true, 사유와 false 실패 반환
                    if (reserves.contains(newReserve))
                        return new ReserveResult(false, "이미 예약된 강의실/시간입니다.");
                    // 최대 예약 개수 초과 시 실패 반환 (학생만 체크)
                    if ("S".equals(role) && reserves.size() >= MAX_RESERVE)
                        return new ReserveResult(false, "최대 4개까지 예약할 수 있습니다.");
                    // 예약 정보 추가
                    reserves.add(newReserve);
                    // 기존 정보 + 예약 정보로 라인 재구성
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 5; i++)
                        sb.append(parts[i]).append(i < 4 ? "," : "");
                    for (String r : reserves)
                        sb.append(",").append(r);
                    lines.add(sb.toString());
                    updated = true;
                } else {
                    // 해당 id가 아니면 그대로 저장
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ReserveResult(false, "파일 읽기 오류");
        }

        // 예약 정보가 갱신된 경우 파일에 다시 저장
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
        // 해당 id를 찾지 못한 경우
        return new ReserveResult(false, "사용자 정보를 찾을 수 없습니다.");
    }

    /**
     * 교수 예약 시 학생 예약 중복 취소 및 해당 학생 ID 리스트 반환
     */
    public static List<String> cancelStudentReservesForProfessor(String roomNumber, String date, String day) {
        List<String> affectedStudentIds = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        String targetReserve = roomNumber + " / " + date + " / " + day;

        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && !"P".equals(parts[4])) { // 학생만
                    List<String> reserves = new ArrayList<>();
                    for (int i = 5; i < parts.length; i++)
                        reserves.add(parts[i]);
                    if (reserves.contains(targetReserve)) {
                        reserves.remove(targetReserve);
                        affectedStudentIds.add(parts[0]);
                    }
                    // 라인 재구성
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 5; i++)
                        sb.append(parts[i]).append(i < 4 ? "," : "");
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

        // 파일에 다시 저장
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

    // id로 예약 정보 조회 - 클라이언트 요청 시 사용
    public static List<String> getReserveInfoById(String id) {
        List<String> reserves = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[0].equals(id)) {
                    for (int i = 5; i < parts.length; i++) {
                        reserves.add(parts[i].trim());
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reserves;
    }

    // 예약 정보로 총 예약자 수 조회 - 클라이언트 요청 시 사용
    public static int countUsersByReserveInfo(String reserveInfo) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // 예약 정보는 6번째부터
                for (int i = 5; i < parts.length; i++) {
                    if (parts[i].trim().equals(reserveInfo.trim())) {
                        count++;
                        break; // 한 사용자가 여러 번 예약해도 1번만 카운트
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    // id와 예약 정보로 예약 취소
    public static ReserveResult cancelReserve(String id, String reserveInfo) {
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[0].equals(id)) {
                    List<String> reserves = new ArrayList<>();
                    for (int i = 5; i < parts.length; i++)
                        reserves.add(parts[i].trim());
                    if (!reserves.contains(reserveInfo.trim())) {
                        return new ReserveResult(false, "해당 예약 정보를 찾을 수 없습니다.");
                    }
                    reserves.remove(reserveInfo.trim());
                    // 라인 재구성
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 5; i++)
                        sb.append(parts[i]).append(i < 4 ? "," : "");
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