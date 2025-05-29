package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import java.io.IOException;

class CheckMaxTimeTest {
    static final Path TEST_USER_FILE = Paths.get(receiveControllerTest.getTestUserFileName());
    static final Path BACKUP_FILE = Paths.get("src/test/resources/UserInfo_test_backup.txt");

    @BeforeEach
    void setUp() throws IOException {
        // 테스트용 유저 데이터 작성 및 백업
        String testData = "S,테스터,20212991,1234,911 / 2025 / 06 / 01 / 12:00 12:50 / 월요일\n";
        Files.writeString(TEST_USER_FILE, testData);
        Files.copy(TEST_USER_FILE, BACKUP_FILE, StandardCopyOption.REPLACE_EXISTING);
    }

    @AfterEach
    void restoreUserFile() throws IOException {
        if (Files.exists(BACKUP_FILE)) {
            Files.copy(BACKUP_FILE, TEST_USER_FILE, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.deleteIfExists(TEST_USER_FILE);
        }
    }

    @Test
    void testCheckMaxTime() {
        CheckMaxTime checker = new CheckMaxTime("20212991");
        boolean exceeded = checker.check();
        assertFalse(exceeded, "최대 예약 시간 초과 여부 확인 실패");
    }
}