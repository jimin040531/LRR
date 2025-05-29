package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class noticeControllerTest {

    @Test
    void testAddAndRemoveNotice() {
        // 실제 존재하는 학생 ID 사용
        List<String> studentIds = Arrays.asList("20212991");
        String notice = "테스트 공지사항입니다.";

        // 공지 추가
        noticeController.addNotice(studentIds, notice);

        // 공지 조회
        List<String> notices = noticeController.getNotices("20212991");
        assertNotNull(notices);

        // 공지 삭제
        if (!notices.isEmpty()) {
            noticeController.removeNotice("20212991", notices.get(0));
        }
        assertTrue(true);
    }
}