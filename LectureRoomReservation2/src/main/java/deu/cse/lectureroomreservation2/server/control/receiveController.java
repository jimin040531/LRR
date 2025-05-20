/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.common.*;

import java.util.List;
/**
 *
 * @author H
 */
public class receiveController {

    // 파일 경로 지정
    private static final String filePath = "src/main/resources/";
    private static final String UserFileName = "UserInfo.txt";

    public static String getFilepath() {
        return filePath;
    }

    public static String getFileName() {
        return UserFileName;
    }

    // 예약 요청 처리
    public ReserveResult handleReserve(ReserveRequest req) {
        /*
         * // 교수 예약 처리 시작
         * if ("P".equals(req.getRole())) {
         * System.out.println("서버 받음 receiveController : "
         * + req.getId() + " " + req.getRole() + " " + req.getRoomNumber() + " " +
         * req.getDate() + " "
         * + req.getDay());
         * 
         * // 예약 처리부분
         * setReserveP reserve = new setReserveP(req.getId(), req.getRoomNumber(),
         * req.getDate(), req.getDay());
         * return new ReserveResult(reserve.getResult(), reserve.getReason());
         * }
         * // 학생 예약 처리 시작
         * else if ("S".equals(req.getRole())) {
         * System.out.println("서버 받음 receiveController : "
         * + req.getId() + " " + req.getRole() + " " + req.getRoomNumber() + " " +
         * req.getDate() + " "
         * + req.getDay());
         * 
         * // 예약 처리
         * setReserveS reserve = new setReserveS(req.getId(), req.getRoomNumber(),
         * req.getDate(), req.getDay());
         * return new ReserveResult(reserve.getResult(), reserve.getReason());
         * } else {
         * return new ReserveResult(false, "역할 오류");
         * }
         */

        if ("P".equals(req.getRole())) {
            // 교수 예약 시 학생 예약 중복 취소
            List<String> affectedStudents = ReserveManager.cancelStudentReservesForProfessor(
                    req.getRoomNumber(), req.getDate(), req.getDay());
            if (!affectedStudents.isEmpty() && req.getNotice() != null && !req.getNotice().isEmpty()) {
                noticeController.addNotice(affectedStudents, req.getNotice());
            }
        }

        // 역할별 분기 없이 ReserveManager에서 처리
        return ReserveManager.reserve(req.getId(), req.getRole(), req.getRoomNumber(), req.getDate(), req.getDay());

    }
}
