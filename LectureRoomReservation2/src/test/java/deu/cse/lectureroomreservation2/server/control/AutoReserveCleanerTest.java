package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import static org.junit.jupiter.api.Assertions.*;

class AutoReserveCleanerTest {
    static final Path TEST_USER_FILE = Paths.get("src/test/resources/UserInfoTest.txt");

    @BeforeEach
    void setUp() throws Exception {
        // 예시 : 아이디 20212991 사용자의 과거 예약 정보 생성
        String pastDate = LocalDate.now().minusDays(2).format(DateTimeFormatter.ofPattern("yyyy / MM / dd"));
        String reservePast = String.format("915 / %s / 09:00 10:00 / 월요일", pastDate);
        String data = "S,심동진,20212991,1234," + reservePast + "\n";
        Files.writeString(TEST_USER_FILE, data);
    }

    @Test
    void testCleanOldReserves_removesPastReservations() throws Exception {
        AutoReserveCleaner cleaner = new AutoReserveCleaner() {
            @Override
            public void run() {
            }
        };
        var method = AutoReserveCleaner.class.getDeclaredMethod("cleanOldReserves");
        method.setAccessible(true);
        method.invoke(cleaner);

        String content = Files.readString(TEST_USER_FILE);
        String pastDate = LocalDate.now().minusDays(2).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        assertFalse(content.contains(pastDate), "과거 예약이 삭제되어야 함");
    }
}