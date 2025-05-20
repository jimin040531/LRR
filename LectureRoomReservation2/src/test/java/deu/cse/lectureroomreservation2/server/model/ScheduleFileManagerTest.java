/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.model;

import deu.cse.lectureroomreservation2.server.control.TimeTableController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jimin
 */
public class ScheduleFileManagerTest {
    
    private ScheduleFileManager fileManager;
    
    private static final Path SCHEDULE_FILE = Paths.get("ScheduleInfo.txt");
    private static final Path BACKUP_FILE = Paths.get("ScheduleInfo_backup.txt");
    
    // 테스트 실행 전 : 파일 백업 + 초기화
    @BeforeEach
    void setUp() throws IOException {
        // 백업
        if (Files.exists(SCHEDULE_FILE)) {
            Files.copy(SCHEDULE_FILE, BACKUP_FILE, StandardCopyOption.REPLACE_EXISTING);
        }
        // 초기화
        Files.write(SCHEDULE_FILE, new ArrayList<>()); // 빈 파일로 초기화
        fileManager = new ScheduleFileManager();       
    }
    
    // 테스트 실행 후 : 파일 백업 복원
    @AfterEach
    void tearDown() throws IOException {
        // 복원
        if (Files.exists(BACKUP_FILE)) {
            Files.copy(BACKUP_FILE, SCHEDULE_FILE, StandardCopyOption.REPLACE_EXISTING);
        }
    }
    
    @Test
    public void testReadAllLines() {
        System.out.println("readAllLines");
        ScheduleFileManager instance = new ScheduleFileManager();
        List expResult = null;
        List result = instance.readAllLines();
        assertEquals(expResult, result);
    }

    @Test
    public void testAppendLine() {
        System.out.println("appendLine");
        String line = "";
        ScheduleFileManager instance = new ScheduleFileManager();
        instance.appendLine(line);
    }

    @Test
    public void testOverwriteAll() {
        System.out.println("overwriteAll");
        List<String> newLines = null;
        ScheduleFileManager instance = new ScheduleFileManager();
        instance.overwriteAll(newLines);
    }
    
}
