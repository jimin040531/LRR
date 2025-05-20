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
public class ChangePassControllerTest {

    ChangePassController controller = new ChangePassController();
    private static final Path USER_FILE = Paths.get("user.txt");    // 기존의 user.txt
    private static final Path BACKUP_FILE = Paths.get("user_backup.txt");   // 테스트용 user.txt

    @BeforeEach
    void backupUserFile() throws IOException {
        Files.copy(USER_FILE, BACKUP_FILE, StandardCopyOption.REPLACE_EXISTING);   // 기존 파일 내용을 테스트용에 복사
    }

    @AfterEach
    void restoreUserFile() throws IOException {
        Files.copy(BACKUP_FILE, USER_FILE, StandardCopyOption.REPLACE_EXISTING);    // 기존 파일에서 작업 후 복사본의 내용을 덮어씀. -> 원점.
    }

    @Test
    public void testChangePassSuccess() {
        String result = controller.changePassword("20212991", "1234", "good");   // user.txt 파일의 정보와 동일하지 않으면 Fail이 발생함.
        assertEquals("비밀번호가 변경되었습니다.", result);  // changePassword() 메서드의 답과 비교하여 동일한지 TEST
    }

    @Test
    public void testWrongCurrentPassword() {
        String result = controller.changePassword("20212991", "wrongpass", "newpass");
        assertEquals("ID 또는 기존 비밀번호가 올바르지 않습니다.", result); // 기존의 비밀번호가 0208이라 TEST 성공
    }

    @Test
    public void testEmptyNewPassword() {
        String result = controller.changePassword("20212991", "0208", "");
        assertEquals("모든 항목을 입력해주세요.", result);  // 모든항목을 입력하지 않았기 때문에 TEST 성공
    }

    @Test
    public void testSamePassword() {
        String result = controller.changePassword("20212991", "0208", "0208");  // 기존과 변경 비밀번호가 동일
        assertEquals("비밀번호가 기존 비밀번호와 동일합니다. 다른 새 비밀번호를 입력해주세요.", result);
    }

    @Test
    public void testIdNotFound() {
        String result = controller.changePassword("99999999", "1234", "newpass");   // ID가 존재 X
        assertEquals("ID 또는 기존 비밀번호가 올바르지 않습니다.", result);
    }
    /*
    RUN 4 , FAIL 3
     */

}
