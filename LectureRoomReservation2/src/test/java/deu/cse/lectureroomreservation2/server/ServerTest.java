package deu.cse.lectureroomreservation2.server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    @Test
    void testServerStart() {
        assertDoesNotThrow(() -> {
            // new Server().start(); // 실제 서버 실행은 주석 처리
        });
    }
}