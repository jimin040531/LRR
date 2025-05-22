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
        // 테스트용 유저 데이터 작성
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

        controller = new LoginController(); // 기본 생성자 사용
    }

    @AfterEach
    void restore() throws IOException {
        if (Files.exists(BACKUP_FILE)) {
            Files.copy(BACKUP_FILE, USER_FILE, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.deleteIfExists(USER_FILE);
        }
    }
    
    @Test
    public void testValidProfessorLogin() {
        LoginStatus result = controller.authenticate("12345", "1234", "P");
        assertTrue(result.isLoginSuccess(), "성공.");
    }

    @Test
    public void testInvalidRoleWordShouldFail() {
        LoginStatus result = controller.authenticate("20210322", "2242", "STUDENT");
        assertFalse(result.isLoginSuccess(), "실패.");
    }

    @Test
    public void testInvalidIdLength() {
        LoginStatus result = controller.authenticate("shortid", "1234", "S");
        assertFalse(result.isLoginSuccess(), "실패.");
    }
    @Test
    public void testValidStudentLoginFail() {
        LoginStatus result = controller.authenticate("20212991", "", "S");
        assertFalse(result.isLoginSuccess(), "실패.");
    }
    @Test
    public void testValidStudentLogin2() {
        LoginStatus result = controller.authenticate("20212977", "1234", "S");
        assertTrue(result.isLoginSuccess(), "성공.");
    }
}
