/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package deu.cse.lectureroomreservation2.client;

import deu.cse.lectureroomreservation2.client.view.LoginView;
import deu.cse.lectureroomreservation2.client.view.MainMenu;
import deu.cse.lectureroomreservation2.server.Server;
import deu.cse.lectureroomreservation2.server.control.LoginStatus;
import lombok.Getter;

/**
 * Client는 강의실 예약 시스템에서 뷰와 관련된 행위를 다룬다. 필요시 Server에 요청하여 필요한 작업을 수행할 수 있다.
 *
 * @author Prof.Jong Min Lee
 */
public class Client {

    private Server server;

    @Getter
    private LoginView loginView = new LoginView();
    private MainMenu mainMenu = new MainMenu();
    private String requestId;
    private String requestPassword;
    @Getter
    private LoginStatus status;

    public Client(Server server) {
        this.server = server;
    }

    public void run() {
        boolean isTestEnvironment = Boolean.parseBoolean(System.getProperty("isTestEnvironment", "false"));
        System.out.println(">>> isTestEnvironment = " + isTestEnvironment);

        if (!isTestEnvironment) {
            loginView.show();
        }
        requestId = loginView.getId();
        requestPassword = loginView.getPassword();
        status = server.requestAuth(requestId, requestPassword);
        if (status.isLoginSuccess()) {
            if (!isTestEnvironment) {
                mainMenu.showMainMenu();
            }
            switch (mainMenu.getSelectedMenu()) {
                case 1 -> {
                    System.out.println("1.강의실 예약을 선택했습니다.");
                }
                case 2 -> {
                    System.out.println("2.예약 목록을 선택했습니다.");
                }
                case 99 -> {
                    System.out.println("99.종료를 선택했습니다.");
                }
            }
            System.out.println("\n\n실행을 종료합니다.");
        } else {
            System.out.println("\n\n로그인 ID나 암호가 맞지 않아 로그인을 실패했습니다.");
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // test code
        Client c = new Client(new Server());
        c.run();
    }

}
