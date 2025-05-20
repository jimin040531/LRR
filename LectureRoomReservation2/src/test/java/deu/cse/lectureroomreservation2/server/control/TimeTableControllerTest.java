/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.server.model.ScheduleFileManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * 테스트 데이터: 911호, 월요일 16:00~16:50, "컴퓨터비전응용", 수업
 *
 * @author Jimin
 */
public class TimeTableControllerTest {
    
    TimeTableController controller;
    private static final Path TEST_FILE = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "test_schedule.txt");

    private final String room = "911";
    private final String day = "월";
    private final String start = "16:00";
    private final String end = "16:50";
    private final String subject = "컴퓨터비전응용";
    private final String type = "수업";

    @BeforeEach
    void setUp() throws IOException {
        if (!Files.exists(TEST_FILE.getParent())) {
            Files.createDirectories(TEST_FILE.getParent());
        }
        Files.write(TEST_FILE, new byte[0]);
        ScheduleFileManager fileManager = new ScheduleFileManager(TEST_FILE.toString());
        controller = new TimeTableController(fileManager);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(TEST_FILE); // 테스트 후 삭제 (원한다면 생략 가능)
    }

    @Test
    public void testLoadSchedulesFromFile() {
        controller.loadSchedulesFromFile();
        assertTrue(true, "파일 로드 성공");
    }

    @Test
    public void testIsScheduleExists() {
        controller.addScheduleToFile(room, day, start, end, subject, type);
        assertTrue(controller.isScheduleExists(room, day, start, end), "시간표 존재");
    }

    @Test
    public void testAddScheduleToFileSuccess() {
        controller.addScheduleToFile(room, day, start, end, subject, type);
        assertTrue(controller.isScheduleExists(room, day, start, end), "시간표 추가 성공");
    }

    @Test
    public void testAddScheduleToFileFailDuplicate() throws IOException {
        controller.addScheduleToFile(room, day, start, end, subject, type);

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            controller.addScheduleToFile(room, day, start, end, subject, type);
        });
        assertEquals("이미 등록된 시간표입니다.", e.getMessage());
    }

    @Test
    public void testDeleteScheduleFromFileSuccess() {
        controller.addScheduleToFile(room, day, start, end, subject, type);
        assertTrue(controller.deleteScheduleFromFile(room, day, start, end), "존재하는 시간표 삭제");
    }

    @Test
    public void testDeleteScheduleFromFileFail() {
        assertFalse(controller.deleteScheduleFromFile(room, day, start, end), "존재하지 않는 시간표 삭제 -> False");
    }

    @Test
    public void testUpdateScheduleSuccess() {
        controller.addScheduleToFile(room, day, start, end, subject, type);
        assertTrue(controller.updateSchedule(room, day, start, end, "소프트웨어공학", type), "일정이 있는 시간표 수정");
    }

    @Test
    public void testUpdateScheduleFail() {
        assertFalse(controller.updateSchedule(room, day, start, end, subject, type), "일정이 없는 시간표 수정 -> False");
    }

    @Test
    public void testGetScheduleForRoom() {
        controller.addScheduleToFile(room, day, start, end, subject, type);
        Map<String, String> result = controller.getScheduleForRoom(room, day, type);
        assertNotNull(result, "시간표 조회 결과 : null X");
        assertTrue(result.containsValue(subject), "조회된 시간표에 추가한 과목명(사유)가 포함되어야 함");
    }

    @Test
    public void testFileContentsAfterAdd() throws IOException {
        controller.addScheduleToFile(room, day, start, end, subject, type);
        String content = Files.readString(TEST_FILE);
        assertTrue(content.contains(subject), "파일에 과목명(사유) 정상적으로 기록되어야 함");
    }
}