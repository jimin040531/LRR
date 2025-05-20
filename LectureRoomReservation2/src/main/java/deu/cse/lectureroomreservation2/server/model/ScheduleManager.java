package deu.cse.lectureroomreservation2.server.model;

import java.util.HashMap;
import java.util.Map;

public class ScheduleManager {
    private Map<String, LectureRoom> lectureRooms;

    public ScheduleManager() {
        this.lectureRooms = new HashMap<>();
    }

    // ✅ 시간표 추가 (수업 또는 제한)
    public void addSchedule(String roomNumber, DaysOfWeek day, String startTime, String endTime, String subject, String type) {
        LectureRoom room = lectureRooms.computeIfAbsent(roomNumber, LectureRoom::new);
        room.addFixedSchedule(day, startTime, endTime, subject, type);
    }

    // ✅ 강의실, 요일, 타입별 시간표 조회
    public Map<String, String> getSchedule(String roomNumber, DaysOfWeek day, String type) {
        LectureRoom room = lectureRooms.get(roomNumber);
        if (room == null) return null;

        return room.getFixedScheduleByType(day, type); // ⬅️ LectureRoom에서 처리
    }
}
