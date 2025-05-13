package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.server.model.DaysOfWeek;
import deu.cse.lectureroomreservation2.server.model.ScheduleEntry;
import deu.cse.lectureroomreservation2.server.model.ScheduleManager;

import java.io.*;
import java.util.Map;

public class TimeTableController {
    private ScheduleManager scheduleManager;
    private final String filePath = System.getProperty("user.dir") + "/src/main/resources/ScheduleInfo.txt";

    public TimeTableController() {
        this.scheduleManager = new ScheduleManager();
    }

    // 📌 파일 → 메모리 로드
    public void loadSchedulesFromFile() {
        this.scheduleManager = new ScheduleManager(); // 기존 정보 초기화

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String roomNumber = parts[0].trim();
                    String day = parts[1].trim();
                    String startTime = parts[2].trim();
                    String endTime = parts[3].trim();
                    String subject = parts[4].trim();
                    String type = parts[5].trim(); // "수업" or "제한"

                    DaysOfWeek dayOfWeek = DaysOfWeek.fromKoreanDay(day);
                    scheduleManager.addSchedule(roomNumber, dayOfWeek, startTime, endTime, subject, type);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("시간표 파일 로딩 실패", e);
        }
    }

    // 📌 해당 시간표 존재 여부 확인
    public boolean isScheduleExists(String roomNumber, String day, String startTime, String endTime) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length >= 4 &&
                    parts[0].trim().equals(roomNumber) &&
                    parts[1].trim().equals(day) &&
                    parts[2].trim().equals(startTime) &&
                    parts[3].trim().equals(endTime)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 📌 시간표 추가
    public void addScheduleToFile(String roomNumber, String day, String startTime, String endTime, String subject, String type) {
        if (roomNumber.isBlank() || day.isBlank() || startTime.isBlank() || endTime.isBlank() || subject.isBlank() || type.isBlank()) {
            throw new IllegalArgumentException("입력값이 누락되었습니다.");
        }

        if (isScheduleExists(roomNumber, day, startTime, endTime)) {
            throw new IllegalArgumentException("이미 등록된 시간표입니다.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(String.join(",", roomNumber, day, startTime, endTime, subject, type));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("시간표 추가 중 오류가 발생했습니다.", e);
        }
    }

    // 📌 시간표 수정 (과목명/종류 덮어쓰기)
    public boolean updateScheduleInFile(String roomNumber, String day, String startTime, String endTime, String newSubject, String newType) {
        return modifyScheduleFile((parts) -> {
            if (isSameSchedule(parts, roomNumber, day, startTime, endTime)) {
                return String.join(",", roomNumber, day, startTime, endTime, newSubject, newType);
            }
            return String.join(",", parts);
        });
    }

    // 📌 시간표 삭제
    public boolean deleteScheduleFromFile(String roomNumber, String day, String startTime, String endTime) {
        return modifyScheduleFile((parts) -> {
            if (isSameSchedule(parts, roomNumber, day, startTime, endTime)) {
                return null; // 삭제
            }
            return String.join(",", parts);
        });
    }

    // 📌 요일/강의실/타입별 시간표 조회
    public Map<String, String> getScheduleForRoom(String roomNumber, String day, String type) {
        DaysOfWeek dayOfWeek = DaysOfWeek.fromKoreanDay(day);
        return scheduleManager.getSchedule(roomNumber, dayOfWeek, type);
    }

    // 🔧 공통 수정 함수
    private boolean modifyScheduleFile(ScheduleLineModifier modifier) {
        boolean modified = false;
        StringBuilder updatedContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(",");
                String result = modifier.modify(parts);
                if (result == null) {
                    modified = true;
                    continue;
                } else if (!String.join(",", parts).equals(result)) {
                    modified = true;
                }
                updatedContent.append(result).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 오류", e);
        }

        if (modified) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(updatedContent.toString());
            } catch (IOException e) {
                throw new RuntimeException("파일 쓰기 오류", e);
            }
        }

        return modified;
    }

    // 🔧 동일한 시간표 항목인지 비교
    private boolean isSameSchedule(String[] parts, String room, String day, String start, String end) {
        return parts.length >= 4 &&
               parts[0].trim().equals(room) &&
               parts[1].trim().equals(day) &&
               parts[2].trim().equals(start) &&
               parts[3].trim().equals(end);
    }

    @FunctionalInterface
    private interface ScheduleLineModifier {
        String modify(String[] parts); // null 반환 시 해당 항목 삭제
    }
}
