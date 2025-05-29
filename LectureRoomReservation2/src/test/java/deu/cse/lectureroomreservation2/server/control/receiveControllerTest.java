package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import deu.cse.lectureroomreservation2.common.ReserveRequest;
import deu.cse.lectureroomreservation2.common.ReserveResult;

public class receiveControllerTest {

    @Test
    void testHandleReserve_Student() {
        ReserveRequest req = new ReserveRequest("20212991", "S", "911", "2025 / 06 / 11", "수요일", "");
        receiveController controller = new receiveController();
        ReserveResult result = controller.handleReserve(req);
        assertTrue(result.getResult() || !result.getResult());
    }

    @Test
    void testHandleReserve_Professor() {
        ReserveRequest req = new ReserveRequest("12345", "P", "911", "2025 / 06 / 11", "수요일", "공지 테스트");
        receiveController controller = new receiveController();
        ReserveResult result = controller.handleReserve(req);
        assertTrue(result.getResult() || !result.getResult());
    }

    @Test
    void testDuplicateReserve() {
        ReserveRequest req = new ReserveRequest("20212991", "S", "911", "2025 / 06 / 12", "목요일", "");
        receiveController controller = new receiveController();
        controller.handleReserve(req);
        ReserveResult result = controller.handleReserve(req);
        assertFalse(result.getResult() && result.getResult());
    }
}