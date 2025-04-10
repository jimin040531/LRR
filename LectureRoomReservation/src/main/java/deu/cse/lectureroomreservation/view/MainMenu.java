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
public class MainMenu {
    @Getter
    private int selectedMenu = -1;
    private static final String[] mainMenu = {
        "1.강의실 예약",
        "2.예약 목록",
        "99.종료"
    };
    
    public void showMainMenu() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("\n\n[주화면]\n\n");
        System.out.println("원하는 메뉴를 선택하세요!");
        for (int i=0; i<mainMenu.length; i++) {
            System.out.printf("    %s%n", mainMenu[i]);
        }
        
        System.out.print("    ===> ");
        selectedMenu = sc.nextInt();
    }
    
    
}
