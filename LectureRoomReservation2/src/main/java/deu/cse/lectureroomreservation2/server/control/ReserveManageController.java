/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.client.Client;
import deu.cse.lectureroomreservation2.common.ReserveManageRequest;
import deu.cse.lectureroomreservation2.common.ReserveManageResult;

/**
 *
 * @author Jimin
 */

/**
 * 예약 관리 기능(조회, 수정, 삭제)을 담당하는 컨트롤러 클래스
 * View는 UI만 처리하고, Controller는 command/요청 생성
 */
public class ReserveManageController {
    private final Client client;

    public ReserveManageController(Client client) {
        this.client = client;
    }

    // 예약 조회 요청
    public ReserveManageResult search(String userId, String room, String date) throws Exception {
        ReserveManageRequest req = new ReserveManageRequest(
                "SEARCH", userId, room, date,
                null, null, null, null, null, null
        );
        return client.sendReserveManageRequest(req);
    }

    // 예약 수정 요청
    public ReserveManageResult update(String userId, String oldReserveInfo,
                                      String newRoom, String newDate, String newWeekDay) throws Exception {
        ReserveManageRequest req = new ReserveManageRequest(
                "UPDATE", userId, null, null,
                oldReserveInfo, newRoom, newDate, newWeekDay, null, null
        );
        return client.sendReserveManageRequest(req);
    }

    // 예약 삭제 요청
    public ReserveManageResult delete(String userId, String reserveInfo) throws Exception {
        ReserveManageRequest req = new ReserveManageRequest(
                "DELETE", userId, null, null,
                null, null, null, null, null, reserveInfo
        );
        return client.sendReserveManageRequest(req);
    }
}
