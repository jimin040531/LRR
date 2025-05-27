package deu.cse.lectureroomreservation2.client.view;

import deu.cse.lectureroomreservation2.client.Client;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuickViewRoomTest {
    @Test
    void testConstructor() {
        Client mockClient = null;
        QuickViewRoom view = new QuickViewRoom(mockClient, "20212991", "S");
        assertNotNull(view);
    }
}