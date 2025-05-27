package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.common.ReserveResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReserveManagerTest {
    @Test
    void testReserveAndCancel() {
        // 실제 환경에 맞게 파라미터 수정
        ReserveResult reserveResult = ReserveManager.reserve("20212991", "S", "915", "2025/06/03", "화요일");
        assertTrue(reserveResult.getResult());

        ReserveResult cancelResult = ReserveManager.cancelReserve("20212991", "915 / 2025 / 06 / 03 / 09:00 10:00 / 화요일");
        assertTrue(cancelResult.getResult());
    }
}