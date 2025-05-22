/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jimin
 */
public class UserRequestControllerTest {
    
    public UserRequestControllerTest() {
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

    @Test
    public void testHandleSearchRequest() {
        System.out.println("handleSearchRequest");
        String roleFilter = "";
        String nameFilter = "";
        UserRequestController instance = new UserRequestController();
        List expResult = null;
        List result = instance.handleSearchRequest(roleFilter, nameFilter);
        assertEquals(expResult, result);
    }

    @Test
    public void testSaveUser() {
        System.out.println("saveUser");
        String[] userData = null;
        UserRequestController instance = new UserRequestController();
        instance.saveUser(userData);
    }

    @Test
    public void testSaveUserAndGetUpdatedList() {
        System.out.println("saveUserAndGetUpdatedList");
        String[] userData = null;
        UserRequestController instance = new UserRequestController();
        List expResult = null;
        List result = instance.saveUserAndGetUpdatedList(userData);
        assertEquals(expResult, result);
    }
    
}
