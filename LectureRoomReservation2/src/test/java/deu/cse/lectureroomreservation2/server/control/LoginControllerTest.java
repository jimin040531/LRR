/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author SAMSUNG
 */
public class LoginControllerTest {

    LoginController controller = new LoginController();

    @BeforeEach
    public void setup() throws IOException {
        String userFileContent = """
        20212991\t1234\tS
        12345\tprofpass\tP
        admin\tadminpass\tA
        20210322\t2242\tS
        """;
        Files.write(Paths.get("user.txt"), userFileContent.getBytes());
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
        assertTrue(result.isLoginSuccess(), "실패");
    }

    @Test
    public void testWrongPassword() {
        LoginStatus result = controller.authenticate("20212991", "1234", "S");
        assertTrue(result.isLoginSuccess(), "성공.");
    }

    //Fail   -> assertTrue로 Test중 실패로 봤기에 실패가 맞음.    
    @Test
    public void testInvalidIdLength() {
        LoginStatus result = controller.authenticate("shortid", "1234", "S");
        assertTrue(result.isLoginSuccess(), "실패.");
    }

    @Test
    public void testValidProfessorLogin() {
        LoginStatus result = controller.authenticate("12345", "profpass", "P");
        assertTrue(result.isLoginSuccess(), "성공.");
    }
    /*
    RUN 5 FAIL 2
     */
}
