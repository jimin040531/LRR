package deu.cse.lectureroomreservation2.client.view;

import deu.cse.lectureroomreservation2.client.Client;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ViewRoomTest {
    @Test
    void testConstructor() {
        Client mockClient = null;
        ViewRoom view = new ViewRoom(mockClient, "20212991", "S", null);
        assertNotNull(view);
    }
}