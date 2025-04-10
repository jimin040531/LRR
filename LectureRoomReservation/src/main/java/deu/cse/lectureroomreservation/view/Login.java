/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation.view;

import java.util.Scanner;
import lombok.Getter;

/**
 *
 * @author Prof.Jong Min Lee
 */
@Getter
public class Login {
    
    private String id;
    private String password;
    
    public void showLoginView() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("\n\n[로그인 화면]\n\n");
        
        System.out.print("* id 입력 : ");
        id = sc.next();
        
        // TODO: 입력받는 암호가 모두 보이면 안 되므로 수정 필요
        System.out.print("* 암호 입력 : ");
        password = sc.next();
    }
            
}
