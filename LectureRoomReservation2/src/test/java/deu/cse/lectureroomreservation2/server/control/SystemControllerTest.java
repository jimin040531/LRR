/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author Prof.Jong Min Lee
 */
public class SystemControllerTest {
    
    public SystemControllerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of requestAuth method, of class SystemController.
     */
    @Test
    @DisplayName("SystemController-인증 01: id와 password가 올바른 경우")
    public void testRequestAuth01() {
        System.out.println("\n\n===> SystemController-인증 01: id와 password가 올바른 경우");
        String id = "tester1";
        String password = "tester1";
        SystemController instance = new SystemController();
        LoginStatus expResult = LoginStatus.builder()
                .loginSuccess(Boolean.TRUE)
                .role("USER")
                .build();
        LoginStatus result = instance.requestAuth(id, password);
        assertEquals(expResult, result);
    }
    
    @Test
    @DisplayName("SystemController-인증 02: id는 있으나, password가 틀린 경우")
    public void testRequestAuth02() {
        System.out.println("\n\n===> SystemController-인증 02: id는 있으나, password가 틀린 경우");
        String id = "tester1";
        String password = "tester1**";
        SystemController instance = new SystemController();
        LoginStatus expResult = LoginStatus.builder()
                .loginSuccess(Boolean.FALSE)
                .role("NONE")
                .build();
        LoginStatus result = instance.requestAuth(id, password);
        assertEquals(expResult, result);
    }
    
    @Test
    @DisplayName("SystemController-인증 03: id나 password가 null인 경우")
    public void testRequestAuth03() {
        System.out.println("\n\n===> SystemController-인증 03: id나 password가 null인 경우");
        String id = null;
        String password = "tester1**";
        SystemController instance = new SystemController();
        LoginStatus expResult = LoginStatus.builder()
                .loginSuccess(Boolean.FALSE)
                .role("NONE")
                .build();
        LoginStatus result = instance.requestAuth(id, password);
        assertEquals(expResult, result);
    }
    
}
