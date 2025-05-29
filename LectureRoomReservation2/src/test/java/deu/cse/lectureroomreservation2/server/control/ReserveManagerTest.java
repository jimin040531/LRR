package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.common.ReserveResult;
import org.junit.jupiter.api.*;
import java.time.*;
import java.time.format.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

class ReserveManagerTest {
    static final Path TEST_USER_FILE = Paths.get(receiveControllerTest.getTestUserFileName());
    static final Path BACKUP_FILE = Paths.get("src/test/resources/UserInfo_test_backup.txt");

    @BeforeEach
    void setUp() throws IOException {
        String testData = "S,테스터,20230001,1234\n";
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
    void GeneraReserve() {
        String nextDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy / MM / dd"));
        ReserveResult result = ReserveManager.reserve("20230001", "S", "911", nextDate, "화요일");
        assertTrue(result.getResult(), "일반 예약은 성공해야 함");
    }

    @Test
    void testReservePast() {
        String pastDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy / MM / dd"));
        ReserveResult pastResult = ReserveManager.reserve("20230001", "S", "911", pastDate, "월요일");
        assertFalse(pastResult.getResult(), "과거 예약은 실패해야 함");
    }

    @Test
    void testReserveWeekend() {
        String weekendDate = "2025 / 06 / 07"; // 토요일
        ReserveResult weekendResult = ReserveManager.reserve("20230001", "S", "911", weekendDate, "토요일");
        assertFalse(weekendResult.getResult(), "주말 예약은 실패해야 함");
    }

    @Test
    void testReserveMaxCount() {
        for (int i = 0; i < 4; i++) {
            String date = LocalDate.now().plusDays(i + 1).format(DateTimeFormatter.ofPattern("yyyy / MM / dd"));
            ReserveManager.reserve("20230001", "S", "911", date, "목요일");
        }
        String overDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("yyyy / MM / dd"));
        ReserveResult overResult = ReserveManager.reserve("20230001", "S", "911", overDate, "목요일");
        assertFalse(overResult.getResult(), "최대 예약 개수 초과 시 실패해야 함");
    }
}