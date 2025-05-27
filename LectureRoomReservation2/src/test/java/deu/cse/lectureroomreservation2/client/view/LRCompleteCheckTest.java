package deu.cse.lectureroomreservation2.client.view;

import deu.cse.lectureroomreservation2.client.Client;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LRCompleteCheckTest {
    @Test
    void testConstructor() {
        Client mockClient = null; // 실제 테스트에서는 Mockito 등으로 Mock 사용
        LRCompleteCheck check = new LRCompleteCheck("20212991", "S", "915", "2025/06/03", "화요일", mockClient, null);
        assertNotNull(check);
        //assertEquals("20212991", check.id);
        //assertEquals("S", check.role);
    }
}
