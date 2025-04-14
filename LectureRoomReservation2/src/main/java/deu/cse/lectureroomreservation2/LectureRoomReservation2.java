/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package deu.cse.lectureroomreservation2;

import deu.cse.lectureroomreservation2.client.Client;
import deu.cse.lectureroomreservation2.server.Server;

/**
 *
 * @author Prof.Jong Min Lee
 */
public class LectureRoomReservation2 {
    
    public void run() {
        Server s = new Server();
        Client c = new Client(s);
        
        c.run();
    }

    public static void main(String[] args) {
        System.out.println("\n\n강의실 예약 시스템 v2.0\n\n");
        
        LectureRoomReservation2 system = new LectureRoomReservation2();
        system.run();
    }
}
