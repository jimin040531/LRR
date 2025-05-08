package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.server.model.DaysOfWeek;
import deu.cse.lectureroomreservation2.server.model.ScheduleManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class TimeTableController {
    private ScheduleManager scheduleManager;

    public TimeTableController() {
        this.scheduleManager = new ScheduleManager();
    }

    // 📌 시간표 파일 경로
    private final String filePath = "C:\\Users\\Jimin\\Documents\\NetBeansProjects\\test\\schedule.txt";

    // 📌 메모리로 시간표 읽기
    public void loadSchedulesFromFile() {        
        this.scheduleManager = new ScheduleManager();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String roomNumber = parts[0].trim();
                    String day = parts[1].trim();
                    String startTime = parts[2].trim();
                    String endTime = parts[3].trim();
                    String subject = parts[4].trim();

                    DaysOfWeek dayOfWeek = DaysOfWeek.fromKoreanDay(day);
                    scheduleManager.addSchedule(roomNumber, dayOfWeek, startTime, endTime, subject);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 📌 시간표 존재 여부 확인
    public boolean isScheduleExists(String roomNumber, String day, String startTime, String endTime) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (isSameSchedule(parts, roomNumber, day, startTime, endTime)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 📌 과목 추가
    public void addScheduleToFile(String roomNumber, String day, String startTime, String endTime, String subject) {
        if (roomNumber.isBlank() || day.isBlank() || startTime.isBlank() || endTime.isBlank() || subject.isBlank()) {
            throw new IllegalArgumentException("입력값이 누락되었습니다.");
        }

        if (isScheduleExists(roomNumber, day, startTime, endTime)) {
            throw new IllegalArgumentException("이미 등록된 시간표입니다.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(String.join(",", roomNumber, day, startTime, endTime, subject));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("시간표 추가 중 오류가 발생했습니다.", e);
        }
    }

    // 📌 과목 수정 (해당 항목을 찾아 과목명 덮어쓰기)
    public boolean updateScheduleInFile(String roomNumber, String day, String startTime, String endTime, String newSubject) {
        return modifyScheduleFile((parts) -> {
            if (isSameSchedule(parts, roomNumber, day, startTime, endTime)) {
                return roomNumber + "," + day + "," + startTime + "," + endTime + "," + newSubject;
            }
            return String.join(",", parts);
        });
    }

    // 📌 과목 삭제
    public boolean deleteScheduleFromFile(String roomNumber, String day, String startTime, String endTime) {
        return modifyScheduleFile((parts) -> {
            if (isSameSchedule(parts, roomNumber, day, startTime, endTime)) {
                return null; // 삭제
            }
            return String.join(",", parts);
        });
    }

    // 📌 강의실/요일 기준 시간표 조회
    public Map<String, String> getScheduleForRoom(String roomNumber, String day) {
        DaysOfWeek dayOfWeek = DaysOfWeek.fromKoreanDay(day);
        return scheduleManager.getSchedule(roomNumber, dayOfWeek);
    }

    // 🔧 내부 공통 처리 메서드
    private boolean modifyScheduleFile(ScheduleLineModifier modifier) {
        boolean modified = false;
        StringBuilder updatedContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(",");
                String result = modifier.modify(parts);
                if (result == null) {
                    modified = true; // 삭제됨
                    continue;
                } else if (!String.join(",", parts).equals(result)) {
                    modified = true; // 수정됨
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

    // 🔧 동일한 시간표인지 확인
    private boolean isSameSchedule(String[] parts, String room, String day, String start, String end) {
        return parts.length >= 4 &&
               parts[0].trim().equals(room) &&
               parts[1].trim().equals(day) &&
               parts[2].trim().equals(start) &&
               parts[3].trim().equals(end);
    }

    // 🔧 라인 수정용 함수형 인터페이스
    @FunctionalInterface
    private interface ScheduleLineModifier {
        String modify(String[] parts); // null이면 삭제
    }
}