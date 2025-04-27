/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.client.view;

import java.util.Scanner;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Prof.Jong Min Lee
 */
@Getter
@Setter
public class LoginView {
    
    private String id;
    private String password;
    private String role;  
    private deu.cse.lectureroomreservation2.server.control.LoginStatus status; 

    public void show() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("\n\n[로그인 화면]\n\n");
        
        System.out.print("* id 입력 : ");
        id = sc.next();
        
        System.out.print("* 암호 입력 : ");
        password = sc.next();
        
        System.out.print("* 역할 입력 (STUDENT/PROFESSOR/ADMIN) : ");
        role = sc.next();
    }
}
