package deu.cse.lectureroomreservation2.server.control;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AutoReserveCleaner extends Thread {
    private final String userFilePath = receiveController.getFilepath() + receiveController.getFileName();

    public AutoReserveCleaner() {
        setDaemon(true); // 서버 종료시 자동 종료
    }

    @Override
    public void run() {
        while (true) {
            try {
                cleanOldReserves();
                System.out.println("오래된 예약 정보 자동 정리 완료");
                Thread.sleep(30 * 1000); // 60초마다 실행 (원하면 더 짧게/길게 조정)
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cleanOldReserves() {
        List<String> lines = new ArrayList<>();
        boolean changed = false; // 변경 여부 플래그

        try (BufferedReader br = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    lines.add(line);
                    continue;
                }
                List<String> validReserves = new ArrayList<>();
                for (int i = 5; i < parts.length; i++) {
                    String reserve = parts[i].trim();
                    String[] reserveParts = reserve.split("/");
                    if (reserveParts.length < 5) {
                        validReserves.add(reserve);
                        continue;
                    }
                    try {
                        String year = reserveParts[1].trim();
                        String month = reserveParts[2].trim();
                        String day = reserveParts[3].trim().split(" ")[0];
                        String timeRange = reserveParts[4].trim();
                        String[] times = timeRange.split(" ");
                        String endTime = times.length > 1 ? times[1] : times[0];
                        String dateTimeStr = year + "-" + month + "-" + day + "T" + endTime;
                        LocalDateTime reserveEndDateTime = LocalDateTime.parse(dateTimeStr,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                        if (reserveEndDateTime.isAfter(LocalDateTime.now())) {
                            validReserves.add(reserve);
                        } else {
                            changed = true; // 삭제된 예약이 있으면 변경 플래그 ON
                        }
                    } catch (Exception e) {
                        validReserves.add(reserve);
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 5; i++)
                    sb.append(parts[i]).append(i < 4 ? "," : "");
                for (String r : validReserves)
                    sb.append(",").append(r);
                lines.add(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 변경이 있을 때만 파일에 다시 저장
        if (changed) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFilePath))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
                System.out.println("오래된 예약 정보가 삭제되어 파일이 갱신되었습니다.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("오래된 예약 정보가 없어 파일을 갱신하지 않았습니다.");
        }
    }
}