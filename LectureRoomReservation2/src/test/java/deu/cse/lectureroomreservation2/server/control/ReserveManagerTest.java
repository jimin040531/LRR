package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.common.ReserveResult;
import org.junit.jupiter.api.*;
import java.time.*;
import java.time.format.*;
import static org.junit.jupiter.api.Assertions.*;

class ReserveManagerTest {
    @Test
    void testReservePastAndWeekend() {
        // 과거 예약
        String pastDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy / MM / dd"));
        ReserveResult pastResult = ReserveManager.reserve("20230001", "S", "911", pastDate, "월요일");
        assertFalse(pastResult.getResult(), "과거 예약은 실패해야 함");

        // 주말 예약
        String weekendDate = "2025/06/07"; // 토요일
        ReserveResult weekendResult = ReserveManager.reserve("20230001", "S", "911", weekendDate, "토요일");
        assertFalse(weekendResult.getResult(), "주말 예약은 실패해야 함");
    }

    @Test
    void testReserveMaxCount() {
        // 최대 예약 개수 초과 시도(가정: 3개)
        for (int i = 0; i < 3; i++) {
            String date = LocalDate.now().plusDays(i + 1).format(DateTimeFormatter.ofPattern("yyyy / MM / dd"));
            ReserveManager.reserve("20230001", "S", "911", date, "목요일");
        }
        String overDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("yyyy / MM / dd"));
        ReserveResult overResult = ReserveManager.reserve("20230001", "S", "911", overDate, "목요일");
        assertFalse(overResult.getResult(), "최대 예약 개수 초과 시 실패해야 함");
    }
}