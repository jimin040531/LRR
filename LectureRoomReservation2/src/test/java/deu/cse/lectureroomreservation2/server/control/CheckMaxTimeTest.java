package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CheckMaxTimeTest {

    @Test
    void testCheckMaxTime() {
        // 실제 UserInfo.txt에 존재하는 ID를 사용해야 합니다.
        String testId = "20212991";
        CheckMaxTime checker = new CheckMaxTime(testId);
        boolean exceeded = checker.check();
        // 예약 4개가 아니면 false, 4개면 true
        assertFalse(exceeded);
    }

    @Test
    void testCheckMaxTimeWithInvalidId() {
        // 존재하지 않는 ID를 사용하여 예외 발생 확인
        String invalidId = "99999999";
        CheckMaxTime checker = new CheckMaxTime(invalidId);
        assertFalse(checker.check());
    }
}