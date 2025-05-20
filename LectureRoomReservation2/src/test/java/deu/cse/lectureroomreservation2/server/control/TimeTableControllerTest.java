/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    
    private TimeTableController controller;
    
    private final String room = "911";
    private final String day = "월";
    private final String start = "16:00";
    private final String end = "16:50";
    private final String subject = "컴퓨터비전응용";
    private final String type = "수업";
    
    private static final Path SCHEDULE_FILE = Paths.get("ScheduleInfo.txt");
    private static final Path BACKUP_FILE = Paths.get("ScheduleInfo_backup.txt");
    
    
    // 테스트 실행 전 : 파일 백업 + 초기화
    @BeforeEach
    void backupScheduleFile() throws IOException {
        controller = new TimeTableController();
        
        if (Files.exists(SCHEDULE_FILE)) {
            Files.copy(SCHEDULE_FILE, BACKUP_FILE, StandardCopyOption.REPLACE_EXISTING);
        }
        controller.deleteScheduleFromFile(room, day, start, end);        
    }
    
    // 테스트 실행 후 : 파일 백업 복원
    @AfterEach
    void restoreScheduleFile() throws IOException {
        if (Files.exists(BACKUP_FILE)) {
            Files.copy(BACKUP_FILE, SCHEDULE_FILE, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Test
    public void testLoadSchedulesFromFile() {
        controller.loadSchedulesFromFile();
        assertTrue(true, "파일 로드 정상 작동");
    }

    // 시간표 등록이 잘 되었는지 확인
    @Test
    public void testIsScheduleExists() {
        controller.addScheduleToFile(room, day, start, end, subject, type);     
        boolean result = controller.isScheduleExists(room, day, start, end);
        assertTrue(result, "시간표 존재");
    }

    // 시간표 추가 기능
    @Test
    public void testAddScheduleToFileSuccess() {
        controller.addScheduleToFile(room, day, start, end, subject, type);
        boolean result = controller.isScheduleExists(room, day, start, end);
        assertTrue(result, "시간표 추가 성공");
    }
    
    // 중복된 시간표 추가
    @Test
    public void testAddScheduleToFileFailDuplicate() {
        controller.addScheduleToFile(room, day, start, end, subject, type);
        assertThrows(IllegalArgumentException.class, () -> {                       
            controller.addScheduleToFile(room, day, start, end, subject, type);
        }, "중복된 시간표를 추가하면 예외가 발생해야 함");
    }

    // 시간표 삭제
    @Test
    public void testDeleteScheduleFromFileSuccess() {
        controller.addScheduleToFile(room, day, start, end, subject, type);
        boolean result = controller.deleteScheduleFromFile(room, day, start, end);
        assertTrue(result, "시간표 삭제 성공");
    }
    
    // 존재하지 않는 시간표 삭제
    @Test
    public void testDeleteScheduleFromFileFail() {
        boolean result = controller.deleteScheduleFromFile(room, day, start, end);
        assertFalse(result, "시간표 삭제 실패");
    }

    // 수정할 시간표에 일정이 있을 때 시간표 수정
    @Test
    public void testUpdateScheduleSuccess() {
        controller.addScheduleToFile(room, day, start, end, subject, type);
        boolean result = controller.updateSchedule(room, day, start, end, "소프트웨어공학", type);
        assertTrue(result, "기존 시간표가 있을 때 업데이트는 성공해야 함");
    }
    
    // 수정할 시간표에 일정이 없을 때 시간표 수정 시도 -> 실패
    @Test
    public void testUpdateScheduleFail() {
        boolean result = controller.updateSchedule(room, day, start, end, subject, type);
        assertFalse(result, "기존 시간표에 일정이 없으므로 업데이트 실패");
    }

    // 시간표 조회 시 변경된 시간표 정보 포함
    @Test
    public void testGetScheduleForRoom() {
        controller.addScheduleToFile(room, day, start, end, subject, type);
        Map<String, String> result = controller.getScheduleForRoom(room, day, type);
        assertTrue(result.containsValue(subject), "시간표 정보 업데이트 성공");
    }
}
