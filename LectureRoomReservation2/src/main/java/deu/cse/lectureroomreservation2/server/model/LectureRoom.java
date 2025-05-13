package deu.cse.lectureroomreservation2.server.model;

import java.util.HashMap;
import java.util.Map;

public class LectureRoom {
    private String roomNumber;
    private Map<DaysOfWeek, Map<String, ScheduleEntry>> fixedSchedule;

    public LectureRoom(String roomNumber) {
        this.roomNumber = roomNumber;
        this.fixedSchedule = new HashMap<>();
        for (DaysOfWeek day : DaysOfWeek.values()) {
            fixedSchedule.put(day, new HashMap<>());
        }
    }

    public void addFixedSchedule(DaysOfWeek day, String startTime, String endTime, String subject, String type) {
        ScheduleEntry entry = new ScheduleEntry(subject, type, startTime, endTime);
        fixedSchedule.get(day).put(startTime, entry);  // ✅ key는 시작 시간만 사용
    }

    public Map<String, ScheduleEntry> getFixedSchedule(DaysOfWeek day) {
        return fixedSchedule.get(day);
    }

    // ✅ 타입에 따른 필터링 제공
    public Map<String, String> getFixedScheduleByType(DaysOfWeek day, String targetType) {
        Map<String, String> filtered = new HashMap<>();
        Map<String, ScheduleEntry> entries = fixedSchedule.get(day);
        for (Map.Entry<String, ScheduleEntry> entry : entries.entrySet()) {
            if (entry.getValue().getType().equals(targetType)) {
                filtered.put(entry.getKey(), entry.getValue().getSubject());
            }
        }
        return filtered;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}

