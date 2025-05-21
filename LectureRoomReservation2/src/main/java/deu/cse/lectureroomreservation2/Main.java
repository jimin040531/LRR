/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package deu.cse.lectureroomreservation2;

import deu.cse.lectureroomreservation2.client.view.LoginFrame;
import deu.cse.lectureroomreservation2.server.Server;
/**
 *
 * @author SAMSUNG
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Server server = new Server();
        new Thread(() -> server.start()).start();
        new LoginFrame().setVisible(true);
    }
    
}
