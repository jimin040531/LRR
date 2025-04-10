/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package deu.cse.lectureroomreservation.model;

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
public class DaysOfWeekTest {
    
    public DaysOfWeekTest() {
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
     * Test of index method, of class DaysOfWeek.
     */
    @Test
    @DisplayName("index test")
    public void testIndex() {
        System.out.println("test index()");
        DaysOfWeek instance = DaysOfWeek.MONDAY;
        int expResult = 0;
        int result = instance.index();
        assertEquals(expResult, result);
    }
    
    @Test
    @DisplayName("index test2")
    public void testIndex2() {
        System.out.println("test index() 2");
        DaysOfWeek instance = DaysOfWeek.FRIDAY;
        int expResult = 4;
        int result = instance.index();
        assertEquals(expResult, result);
    }
    
}
