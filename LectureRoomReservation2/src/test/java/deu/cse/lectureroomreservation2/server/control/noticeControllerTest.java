package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class noticeControllerTest {
    @Test
    void testAddAndGetNotice() {
        noticeController.addNotice(List.of("20212991"), "테스트 공지");
        List<String> notices = noticeController.getNotices("20212991");
        assertTrue(notices.contains("테스트 공지"));
    }
}