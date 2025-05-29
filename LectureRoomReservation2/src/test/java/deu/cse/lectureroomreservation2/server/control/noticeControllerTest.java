package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

class noticeControllerTest {
    static final Path TEST_NOTICE_FILE = Paths.get(receiveControllerTest.getTestNoticeFileName());
    static final Path BACKUP_FILE = Paths.get("src/test/resources/noticeSave_test_backup.txt");

    @BeforeEach
    void setUp() throws Exception {
        Files.writeString(TEST_NOTICE_FILE, "");
        Files.copy(TEST_NOTICE_FILE, BACKUP_FILE, StandardCopyOption.REPLACE_EXISTING);
    }

    @AfterEach
    void restoreNoticeFile() throws Exception {
        if (Files.exists(BACKUP_FILE)) {
            Files.copy(BACKUP_FILE, TEST_NOTICE_FILE, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.deleteIfExists(TEST_NOTICE_FILE);
        }
    }

    @Test
    void testAddNotice() throws Exception {
        // 1. 공지 추가
        List<String> users = List.of("20230001");
        String notice = "테스트 공지";
        noticeController.addNotice(users, notice);
    }

    @Test
    void testGetNotice() throws Exception {
        // 2. 공지 조회
        List<String> users = List.of("20230001");
        String notice = "테스트 공지";
        noticeController.addNotice(users, notice);
        
        List<String> notices = noticeController.getNotices("20230001");
        assertFalse(notices.isEmpty(), "공지 조회는 비어있지 않아야 함");
        assertEquals(notice, notices.get(0), "공지 내용이 일치해야 함");
    }
}