package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheckMaxTimeTest {
    @Test
    void testCheckMaxTime() {
        CheckMaxTime checker = new CheckMaxTime("20212991");
        boolean exceeded = checker.check();
        // assertTrue/False 등으로 검증
    }
}