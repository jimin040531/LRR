/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation.control;

import deu.cse.lectureroomreservation.LectureRoomTest;
import deu.cse.lectureroomreservation.model.LectureRoom;
import deu.cse.lectureroomreservation.model.ReservationAgent;
import deu.cse.lectureroomreservation.view.MainMenu;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Prof.Jong Min Lee
 */
@Slf4j
public class SystemController {

    private Boolean wantToQuit = false;
    private MainMenu mainMenu = new MainMenu();
    private ReservationAgent agent;

    private LoginController loginController = new LoginController();
    private ReservationController reservationController;

    /**
     * 시스템 실행에 필요한 초기화를 수행한다.
     */
    public SystemController() {
        List<LectureRoom> lectureRoomList;
        try {
            lectureRoomList = LectureRoomTest.initializeUsingYaml("2025학년도", "1학기");
            agent = new ReservationAgent(lectureRoomList);
            reservationController = new ReservationController(agent);
        } catch (Exception ex) {
            log.error("예외 발생: {}", ex.getMessage());
        }

    }

    public void run() {
        // 메뉴 선택과 실행(반복)
        while (!wantToQuit) {
            // 로그인 절차 수행
            LoginStatus loginStatus = loginController.processLogin();

            if (!loginStatus.isLoginSuccess()) {
                // 로그인 실패했으므로 다시 로그인화면을 보여줌.
                System.out.println(">> ID 또는 암호가 일치하지 않아 로그인을 다시 하기 바랍니다!");
                continue;
            }

            mainMenu.showMainMenu();
            if (mainMenu.getSelectedMenu() == 99) {
                System.out.println(">> 강의실 예약 프로그램을 종료합니다.");
                wantToQuit = true;
            } else {
                performSelectedMenu();
            }
        }
    }

    private Boolean performSelectedMenu() {
        Boolean resultStatus = false;

        // NOTE: 이런 방식으로 switch 문을 작성하면 break 문이 없어도 됨.
        switch (mainMenu.getSelectedMenu()) {
            case 1 -> {
                System.out.println("\n\n1. 강의실 예약을 선택하였습니다.");
            }
            case 2 -> {
                System.out.println("\n\n2. 예약 목록 보기를 선택하였습니다.");
            }
            default -> {
                System.out.printf(">> 잘못 선택되었습니다: selectedMenu = %d%n", mainMenu.getSelectedMenu());
            }
        }

        return resultStatus;
    }
}
