package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class noticeControllerTest {
    @Test
    void testAddAndGetNotice() throws Exception {
        // 1. 공지 추가
        List<String> users = List.of("20230001");
        String notice = "테스트 공지";
        noticeController.addNotice(users, notice);

        // 2. 공지 확인
        List<String> savedNotice = noticeController.getNotices("20230001");
        assertEquals(notice, savedNotice, "공지사항이 정상적으로 저장되어야 함");
    }
}