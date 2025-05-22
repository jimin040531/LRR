/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.model;

import java.util.Map;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 
 * @author Jimin
 */
public class ScheduleManagerTest {
    
    private ScheduleManager schedule;

    @BeforeEach
    public void setUp() {
        schedule = new ScheduleManager();
    }

    // 기존 시간표에 있는 일정 추가 -> getSchedule()로 조회
    @Test
    public void testAddSchedule() {
        String roomNumber = "911";
        DaysOfWeek day = DaysOfWeek.fromKoreanDay("월");
        String startTime = "16:00";
        String endTime = "16:50";
        String subject = "컴퓨터비전응용";
        String type = "수업";

        // 일정 추가
        schedule.addSchedule(roomNumber, day, startTime, endTime, subject, type);

        Map<String, String> scheduleMap = schedule.getSchedule(roomNumber, day, type);

        assertNotNull(scheduleMap, "getSchedule 결과가 null이면 안 됨");
        assertTrue(scheduleMap.containsKey(startTime), "추가한 시작 시간이 존재해야 함");
        assertEquals(subject, scheduleMap.get(startTime), "추가한 과목명과 일치해야 함");
    }

    @Test
    public void testGetSchedule_empty() {
        String roomNumber = "999";  // 존재하지 않는 강의실 번호
        DaysOfWeek day = DaysOfWeek.fromKoreanDay("화");
        String type = "수업";

        Map<String, String> result = schedule.getSchedule(roomNumber, day, type);

        assertTrue(result == null || result.isEmpty(), "일정이 없을 경우 -> null 또는 빈 Map");
    }
}
