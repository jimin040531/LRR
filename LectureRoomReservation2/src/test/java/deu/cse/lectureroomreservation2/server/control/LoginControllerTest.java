/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import java.io.IOException;
import java.nio.file.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author SAMSUNG
 */
public class LoginControllerTest {

    private LoginController controller;
    private static final Path USER_FILE = Paths.get("UserInfo.txt");
    private static final Path BACKUP_FILE = Paths.get("UserInfo_backup.txt");

    @BeforeEach
    void setup() throws IOException {
        // 기존 user.txt 백업
        if (Files.exists(USER_FILE)) {
            Files.copy(USER_FILE, BACKUP_FILE, StandardCopyOption.REPLACE_EXISTING);
        }

        // 테스트용 유저 데이터 생성
        String userFileContent = """
            20212991,1234,S
            12345,1234,P
            23456,1234,P
            20212977,1234,S
            """;
        Files.write(USER_FILE, userFileContent.getBytes());

        controller = new LoginController(); // LoginController는 기본 생성자 사용
    }

    @AfterEach
    void restore() throws IOException {
        if (Files.exists(BACKUP_FILE)) {
            Files.copy(BACKUP_FILE, USER_FILE, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.deleteIfExists(USER_FILE); // 백업이 없다면 그냥 삭제
        }
    }

    /**
     * 테스트용이기 때문에 True,False를 따지지않고 True만을 사용하여 성공 실패를 판별함.
     *
     */
    @Test
    public void testValidStudentLogin() {
        LoginStatus result = controller.authenticate("20212991", "1234", "S");
        assertTrue(result.isLoginSuccess(), "성공.");
    }

    //Fail   -> assertTrue로 Test중 실패로 봤기에 실패가 맞음.
    @Test
    public void testInvalidRoleWordShouldFail() {
        LoginStatus result = controller.authenticate("20210322", "2242", "STUDENT");
        assertFalse(result.isLoginSuccess(), "실패");
    }

    //Fail   -> assertTrue로 Test중 실패로 봤기에 실패가 맞음.    
    @Test
    public void testInvalidIdLength() {
        LoginStatus result = controller.authenticate("shortid", "1234", "S");
        assertFalse(result.isLoginSuccess(), "실패.");
    }

    @Test
    public void testValidProfessorLogin() {
        LoginStatus result = controller.authenticate("12345", "1234", "P");
        assertTrue(result.isLoginSuccess(), "성공.");
    }
}
