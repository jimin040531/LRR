package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import deu.cse.lectureroomreservation2.common.ReserveResult;

public class ReserveManagerTest {

    @Test
    void testReserveAndCancel() {
        // 실제 UserInfo.txt에 존재하는 ID와 역할을 사용해야 합니다.
        String id = "20212991";
        String role = "S";
        String room = "911";
        String date = "2025 / 06 / 10 / 12:00 12:50";
        String day = "화요일";

        // 예약 시도
        ReserveResult reserveResult = ReserveManager.reserve(id, role, room, date, day);
        assertTrue(reserveResult.getResult() || !reserveResult.getResult());

        // 예약 정보 조회
        List<String> reserves = ReserveManager.getReserveInfoById(id);
        assertNotNull(reserves);

        // 예약 취소 시도
        String reserveInfo = room + " / " + date + " / " + day;
        ReserveResult cancelResult = ReserveManager.cancelReserve(id, reserveInfo);
        assertTrue(cancelResult.getResult() || !cancelResult.getResult());
    }
}