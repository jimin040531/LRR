package deu.cse.lectureroomreservation2.server.model;

import java.util.HashMap;
import java.util.Map;

public class ScheduleManager {
    private Map<String, LectureRoom> lectureRooms;

    public ScheduleManager() {
        this.lectureRooms = new HashMap<>();
    }

    // 강의실 추가
    public void addSchedule(String roomNumber, DaysOfWeek day, String startTime, String endTime, String subject) {
        LectureRoom room = lectureRooms.computeIfAbsent(roomNumber, LectureRoom::new);
        room.addFixedSchedule(day, startTime, endTime, subject);
    }

    // 강의실 시간표 조회
    public Map<String, String> getSchedule(String roomNumber, DaysOfWeek day) {
        LectureRoom room = lectureRooms.get(roomNumber);
        if (room != null) {
            return room.getFixedSchedule(day);
        }
        return null;
    }
}
