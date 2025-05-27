package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import deu.cse.lectureroomreservation2.common.ReserveRequest;
import deu.cse.lectureroomreservation2.common.ReserveResult;

class receiveControllerTest {
    @Test
    void testHandleReserve() {
        ReserveRequest req = new ReserveRequest("20212991", "S", "915", "2025/06/03", "화요일", "공지");
        receiveController controller = new receiveController();
        ReserveResult result = controller.handleReserve(req);
        assertTrue(result.getResult());
    }
    @Test

    void testHandleCancel() {
        ReserveRequest req = new ReserveRequest("20212991", "S", "915", "2025/06/03", "화요일", "취소");
        receiveController RCcontroller = new receiveController();
        ReserveManager RMcontroller = new ReserveManager();
        RMcontroller.reserve("20212991", "S", "915", "2025/06/03", "화요일");
        //ReserveResult result = RCcontroller.handleCancel(req);
    }
}