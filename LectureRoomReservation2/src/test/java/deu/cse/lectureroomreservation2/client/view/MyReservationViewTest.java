package deu.cse.lectureroomreservation2.client.view;

import deu.cse.lectureroomreservation2.client.Client;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyReservationViewTest {
    @Test
    void testConstructor() {
        Client mockClient = null;
        MyReservationView view = new MyReservationView(mockClient, "20212991", "S");
        assertNotNull(view);
    }
}