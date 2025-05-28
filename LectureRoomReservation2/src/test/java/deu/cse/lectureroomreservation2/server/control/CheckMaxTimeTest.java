package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheckMaxTimeTest {
    @Test
    void testCheckMaxTime() {
        CheckMaxTime checker = new CheckMaxTime("20212991");
        boolean exceeded = checker.check();
        assertFalse(exceeded, "최대 예약 시간 초과 여부 확인 실패");
    }
}