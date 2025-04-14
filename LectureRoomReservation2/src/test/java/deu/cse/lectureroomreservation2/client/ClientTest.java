/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package deu.cse.lectureroomreservation2.client;

import deu.cse.lectureroomreservation2.server.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author Prof.Jong Min Lee
 */
public class ClientTest {
    
    public ClientTest() {
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
     * Test of run method, of class Client.
     */
    @Test
    @DisplayName("admin 계정으로 정상 로그인하는 경우")
    public void testRun01() {
        System.out.println("\n\n===> admin 계정으로 정상 로그인하는 경우");
        System.setProperty("isTestEnvironment", "true");
        Server server = new Server();
        Client instance = new Client(server);
        instance.getLoginView().setId("admin");
        instance.getLoginView().setPassword("admin");
        instance.run();
        assertTrue(instance.getStatus().isLoginSuccess());
        
    }
    
    @Test
    @DisplayName("admin 계정으로 로그인 실패하는 경우")
    public void testRun02() {
        System.out.println("\n\n===> admin 계정으로 로그인 실패하는 경우");
        System.setProperty("isTestEnvironment", "true");
        Server server = new Server();
        Client instance = new Client(server);
        instance.getLoginView().setId("admin");
        instance.getLoginView().setPassword("admin***");
        instance.run();
        assertFalse(instance.getStatus().isLoginSuccess());
        
    }

    
}
