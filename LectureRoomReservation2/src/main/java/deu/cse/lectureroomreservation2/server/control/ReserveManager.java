package deu.cse.lectureroomreservation2.server.control;

import java.io.*;
import java.util.*;
import deu.cse.lectureroomreservation2.common.*;

public class ReserveManager {
    // 사용자 정보 파일 경로 (예약 정보도 이 파일에 저장)
    private static final String USER_FILE = receiveController.getFilepath() + "UserInfotest.txt";
    private static final int MAX_RESERVE = 4; // 최대 예약 개수

    /**
     * 예약 요청을 처리하는 메서드입니다.
     * @param id 사용자 ID
     * @param role 사용자 역할(학생/교수)
     * @param roomNumber 강의실 번호
     * @param date 예약 날짜
     * @param day 예약 요일
     * @return ReserveResult(예약 성공/실패 및 사유)
     */
    public static ReserveResult reserve(String id, String role, String roomNumber, String date, String day) {
        List<String> lines = new ArrayList<>(); // 파일 전체 라인 저장
        boolean updated = false; // 예약 정보 갱신 여부
        String newReserve = roomNumber + " / " + date + " / " + day; // 예약 정보 문자열

        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // 해당 id의 사용자를 찾으면 예약 정보 처리
                if (parts.length >= 5 && parts[0].equals(id)) {
                    List<String> reserves = new ArrayList<>();
                    for (int i = 5; i < parts.length; i++) reserves.add(parts[i]);
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
                    for (int i = 0; i < 5; i++) sb.append(parts[i]).append(i < 4 ? "," : "");
                    for (String r : reserves) sb.append(",").append(r);
                    lines.add(sb.toString());
                    updated = true;
                } else {
                    // 해당 id가 아니면 그대로 저장ㅇㅇ
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
}