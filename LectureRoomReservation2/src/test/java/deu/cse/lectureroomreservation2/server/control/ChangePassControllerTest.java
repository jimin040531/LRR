/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author SAMSUNG
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangePassControllerTest {

    ChangePassController controller = new ChangePassController();
    private static final Path USER_FILE = Paths.get("src/main/resources/UserInfo.txt");
    private static final Path BACKUP_FILE = Paths.get("src/main/resources/UserInfo_backup.txt");

    @BeforeEach
    void setup() throws IOException {
        // 테스트용 유저 데이터 덮어쓰기
        String testData = """
            P,김성우,12345,1234
            S,이지민,20233065,1234
            P,권순각,23456,1234
            S,이규찬,20212977,1234
            S,심동진,20212991,1234
            """;
        Files.writeString(USER_FILE, testData);

        // 백업 저장
        Files.copy(USER_FILE, BACKUP_FILE, StandardCopyOption.REPLACE_EXISTING);
    }

    @AfterEach
    void restoreUserFile() throws IOException {
        if (Files.exists(BACKUP_FILE)) {
            Files.copy(BACKUP_FILE, USER_FILE, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.deleteIfExists(USER_FILE);
        }
    }

    @Test
    public void testCurrentPassword() {
        String result = controller.changePassword("20212991", "1234", "1111");
        assertEquals("비밀번호가 변경되었습니다.", result);
    }

    @Test
    public void testWrongCurrentPassword() {
        String result = controller.changePassword("20212991", "Wrongpass", "newpass");
        assertEquals("ID 또는 기존 비밀번호가 올바르지 않습니다.", result);
    }

    @Test
    public void testEmptyNewPassword() {
        String result = controller.changePassword("20212991", "1234", "");
        assertEquals("모든 항목을 입력해주세요.", result);
    }

    @Test
    public void testSamePassword() {
        String result = controller.changePassword("20212991", "1234", "1234");
        assertEquals("비밀번호가 기존 비밀번호와 동일합니다. 다른 새 비밀번호를 입력해주세요.", result);
    }

    @Test
    public void testIdNotFound() {
        String result = controller.changePassword("99999999", "1234", "newpass");
        assertEquals("ID 또는 기존 비밀번호가 올바르지 않습니다.", result);
    }
}
