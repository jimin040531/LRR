/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package deu.cse.lectureroomreservation2.server;

import deu.cse.lectureroomreservation2.server.control.LoginStatus;
import deu.cse.lectureroomreservation2.server.control.SystemController;

/**
 *
 * @author Prof.Jong Min Lee
 */
public class Server {

    private SystemController controller;

    public Server() {
        controller = new SystemController();
    }

    public LoginStatus requestAuth(String id, String password, String selectedRole) {
        System.out.printf(">>> id = %s, password = %s, selected = %s%n%n", id, password, selectedRole);
        return controller.requestAuth(id, password, role);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
