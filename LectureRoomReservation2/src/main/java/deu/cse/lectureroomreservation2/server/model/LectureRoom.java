package deu.cse.lectureroomreservation2.server.model;

import java.util.HashMap;
import java.util.Map;

public class LectureRoom {
    private String roomNumber;
    private Map<DaysOfWeek, Map<String, String>> fixedSchedule;

    public LectureRoom(String roomNumber) {
        this.roomNumber = roomNumber;
        this.fixedSchedule = new HashMap<>();
        for (DaysOfWeek day : DaysOfWeek.values()) {
            fixedSchedule.put(day, new HashMap<>());
        }
    }

    // 고정 시간표 추가
    public void addFixedSchedule(DaysOfWeek day, String startTime, String endTime, String subject) {
        String timeSlot = startTime + "-" + endTime;
        fixedSchedule.get(day).put(timeSlot, subject);
    }

    // 고정 시간표 조회
    public Map<String, String> getFixedSchedule(DaysOfWeek day) {
        return fixedSchedule.get(day);
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}
