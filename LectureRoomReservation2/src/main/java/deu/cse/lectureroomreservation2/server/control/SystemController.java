/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.LectureRoomTest;
import deu.cse.lectureroomreservation2.server.model.LectureRoom;
import deu.cse.lectureroomreservation2.server.model.ReservationAgent;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Prof.Jong Min Lee
 */
@Slf4j
public class SystemController {
    private Boolean wantToQuit = false;

    private LoginController loginController = new LoginController();
    
    private ReservationAgent agent;
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
    
    public LoginStatus requestAuth(String id, String password) {
        LoginStatus status = loginController.authenticate(id, password);
        return status;
    }
    
}
