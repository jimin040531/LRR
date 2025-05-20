/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.server.model.DaysOfWeek;
import deu.cse.lectureroomreservation2.server.model.ScheduleFileManager;
import deu.cse.lectureroomreservation2.server.model.ScheduleManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jimin
 */
public class TimeTableController {
    private final ScheduleFileManager fileManager;
    private ScheduleManager scheduleManager;

    public TimeTableController() {
        this.fileManager = new ScheduleFileManager();
        this.scheduleManager = new ScheduleManager();
    }

    public void loadSchedulesFromFile() {
            scheduleManager = new ScheduleManager();
            List<String[]> rawLines = fileManager.readAllLines();
            for (String[] parts : rawLines) {
                if (parts.length == 6) {
                    String room = parts[0].trim();
                    DaysOfWeek day = DaysOfWeek.fromKoreanDay(parts[1].trim());
                    String start = parts[2].trim();
                    String end = parts[3].trim();
                    String subject = parts[4].trim();
                    String type = parts[5].trim();
                    scheduleManager.addSchedule(room, day, start, end, subject, type);
                }
            }
        }

    public boolean isScheduleExists(String room, String day, String start, String end) {
            List<String[]> lines = fileManager.readAllLines();
            for (String[] parts : lines) {
                if (parts.length >= 4 &&
                    parts[0].trim().equals(room) &&
                    parts[1].trim().equals(day) &&
                    parts[2].trim().equals(start) &&
                    parts[3].trim().equals(end)) {
                    return true;
                }
            }
            return false;
        }

     public void addScheduleToFile(String room, String day, String start, String end, String subject, String type) {
            if (isScheduleExists(room, day, start, end)) {
                throw new IllegalArgumentException("이미 등록된 시간표입니다.");
            }

            String line = String.join(",", room.trim(), day.trim(), start.trim(), end.trim(), subject.trim(), type.trim());
            fileManager.appendLine(line);
        }

    public boolean deleteScheduleFromFile(String room, String day, String start, String end) {
            List<String[]> lines = fileManager.readAllLines();
            List<String> updated = new ArrayList<>();
            boolean deleted = false;

            for (String[] parts : lines) {
                if (parts.length >= 4 &&
                    parts[0].trim().equals(room) &&
                    parts[1].trim().equals(day) &&
                    parts[2].trim().equals(start) &&
                    parts[3].trim().equals(end)) {
                    deleted = true; // 해당 항목 삭제
                    continue;
                }
                updated.add(String.join(",", parts));
            }

            if (deleted) {
                fileManager.overwriteAll(updated);
            }

            return deleted;
        }

    public boolean updateSchedule(String room, String day, String start, String end, String subject, String type) {
            boolean deleted = deleteScheduleFromFile(room, day, start, end);
            if (deleted) {
                addScheduleToFile(room, day, start, end, subject, type);
                return true;
            }
            return false;
        }

    public Map<String, String> getScheduleForRoom(String room, String day, String type) {
            loadSchedulesFromFile(); // 항상 최신화
            DaysOfWeek dayOfWeek = DaysOfWeek.fromKoreanDay(day);
            return scheduleManager.getSchedule(room, dayOfWeek, type);
    }
}