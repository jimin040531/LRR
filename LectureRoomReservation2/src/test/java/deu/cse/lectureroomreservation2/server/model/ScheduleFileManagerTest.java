/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 
 * @author Jimin
 */
public class ScheduleFileManagerTest {

    // 상대경로로 테스트용 파일 경로 지정
    private static final Path TEST_FILE = Paths.get("src/test/resources/test_schedule.txt");
    private ScheduleFileManager fileManager;

    @BeforeEach
    void setUp() throws IOException {
        if (!Files.exists(TEST_FILE.getParent())) {
            Files.createDirectories(TEST_FILE.getParent());
        }
        Files.write(TEST_FILE, new byte[0]); // 빈 파일 생성
        fileManager = new ScheduleFileManager(TEST_FILE.toString());
    }

    @Test
    public void testReadAllLines_emptyFile() {
        List<String[]> lines = fileManager.readAllLines();
        assertNotNull(lines, "readAllLines -> null X (List를 반환해야 함)");
        assertTrue(lines.isEmpty(), "초기 빈 파일은 내용이 없어야 함");
    }

    @Test
    public void testAppendLine_andRead() {
        String testLine = "911,월,16:00,16:50,컴퓨터비전응용,수업";
        fileManager.appendLine(testLine);

        List<String[]> lines = fileManager.readAllLines();
        assertEquals(1, lines.size(), "1줄 추가 완료");
        assertArrayEquals(testLine.split(","), lines.get(0), "추가된 내용이 일치해야 함");
    }

    @Test
    public void testOverwriteAll() {
        List<String> newLines = List.of(
            "911,월,16:00,16:50,컴퓨터비전응용,수업",
            "912,화,09:00,09:50,자료구조,수업"
        );
        fileManager.overwriteAll(newLines);

        List<String[]> lines = fileManager.readAllLines();
        assertEquals(newLines.size(), lines.size(), "덮어쓴 라인 수가 일치해야 함");
        for (int i = 0; i < newLines.size(); i++) {
            assertArrayEquals(newLines.get(i).split(","), lines.get(i), "각 줄 내용이 일치해야 함");
        }
    }
}
