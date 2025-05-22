/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server;

/**
 *
 * @author SAMSUNG
 */
import deu.cse.lectureroomreservation2.common.LoginStatus;
import deu.cse.lectureroomreservation2.server.control.noticeController;
import deu.cse.lectureroomreservation2.server.control.receiveController;
import deu.cse.lectureroomreservation2.server.control.CheckMaxTime;
import deu.cse.lectureroomreservation2.server.control.ReserveManager;
import deu.cse.lectureroomreservation2.common.ReserveResult;
import deu.cse.lectureroomreservation2.common.CheckMaxTimeResult;
import deu.cse.lectureroomreservation2.common.ReserveRequest;
import deu.cse.lectureroomreservation2.common.CheckMaxTimeRequest;
import deu.cse.lectureroomreservation2.common.ScheduleRequest;
import deu.cse.lectureroomreservation2.common.ScheduleResult;
import deu.cse.lectureroomreservation2.common.UserRequest;
import deu.cse.lectureroomreservation2.common.UserResult;
import deu.cse.lectureroomreservation2.server.control.TimeTableController;
import deu.cse.lectureroomreservation2.server.control.UserRequestController;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Server server;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        boolean acquired = false;
        String id = null;

        try {
            System.out.println("Client Connection request received: " + socket.getInetAddress());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // 사용자 정보 먼저 받음
            id = in.readUTF();
            String password = in.readUTF();
            String role = in.readUTF();

            // 세마포어 체크는 로그인 정보 받고 나서 수행
            acquired = server.getConnectionLimiter().tryAcquire();
            if (!acquired) {
                System.out.println("Connection refused (Max count exceed): " + id);
                out.writeObject(new LoginStatus(false, "WAIT", "현재 접속 인원이 가득 찼습니다. 잠시 후 다시 시도해 주세요."));
                out.flush();
                return;
            }

            // 중복로그인 체크
            synchronized (server.getLoggedInUsers()) {
                if (server.getLoggedInUsers().contains(id)) {
                    System.out.println("Connection refused (account already log-in): " + id);
                    out.writeObject(new LoginStatus(false, "DUPLICATE", "이미 로그인 중인 계정입니다."));
                    out.flush();
                    return;
                }
            }

            LoginStatus status = server.requestAuth(id, password, role); // 인증
            if (status.isLoginSuccess()) {
                synchronized (server.getLoggedInUsers()) {
                    server.getLoggedInUsers().add(id); // 로그인 성공한 사용자 등록
                }
            }

            out.writeObject(status);
            out.flush();

            // 로그인 성공한 경우 명령 수신 루프
            if (status.isLoginSuccess()) {
                // 공지사항 수신 및 표시
                System.out.println("로그인 성공 하여 역할 " + status.getRole() + "를 가집니다.");
                if ("STUDENT".equals(status.getRole())) {
                    List<String> notices = noticeController.getNotices(id);
                    for (String notice : notices) {
                        out.writeUTF("NOTICE");
                        out.flush();
                        out.writeUTF(notice);
                        out.flush();
                        noticeController.removeNotice(id, notice);
                    }
                    out.writeUTF("NOTICE_END");
                    out.flush();
                }

                while (true) {
                    try {
                        String command = in.readUTF();
                        if ("LOGOUT".equalsIgnoreCase(command)) {
                            System.out.println("User has log-out: " + id);
                            break;
                        }
                        // 예약 요청 처리
                        if ("RESERVE".equals(command)) {
                            // 클라이언트로부터 예약 요청 객체를 받음
                            ReserveRequest req = (ReserveRequest) in.readObject();
                            // 예약 처리 결과를 받아옴
                            ReserveResult result = new receiveController().handleReserve(req);
                            // 결과를 클라이언트에 전송
                            out.writeObject(result);
                            out.flush();
                        }
                        // CHECK_MAX_TIME 명령 처리 추가
                        if ("CHECK_MAX_TIME".equals(command)) {
                            CheckMaxTimeRequest req = (CheckMaxTimeRequest) in.readObject();
                            boolean exceeded = new CheckMaxTime(req.getId()).check();

                            String reason = exceeded ? "최대 예약 가능 개수를 초과했습니다." : "예약 가능";

                            CheckMaxTimeResult result = new CheckMaxTimeResult(exceeded, reason);
                            out.writeObject(result);
                            out.flush();
                        }
                        // 클라이언트 요청 - id로 예약 정보 조회 요청 받는 부분
                        if ("RETRIEVE_MY_RESERVE".equals(command)) {
                            String userId = in.readUTF();
                            List<String> reserves = ReserveManager.getReserveInfoById(userId);
                            out.writeObject(reserves);
                            out.flush();
                        }
                        // 클라이언트 요청 - 예약 정보로 총 예약자 수 조회 요청 받는 부분
                        if ("COUNT_RESERVE_USERS".equals(command)) {
                            String reserveInfo = in.readUTF();
                            int userCount = ReserveManager.countUsersByReserveInfo(reserveInfo);
                            out.writeInt(userCount);
                            out.flush();
                        }
                        // 클라이언트 요청 - 예약 정보로 예약자 id 목록 조회 (6번 기능)
                        if ("GET_USER_IDS_BY_RESERVE".equals(command)) {
                            String reserveInfo = in.readUTF();
                            List<String> userIds = ReserveManager.getUserIdsByReserveInfo(reserveInfo);
                            out.writeObject(userIds);
                            out.flush();
                        }
                        // 클라이언트 요청 - 예약 취소 요청 받는 부분
                        if ("CANCEL_RESERVE".equals(command)) {
                            String userId = in.readUTF();
                            String reserveInfo = in.readUTF();
                            ReserveResult result = ReserveManager.cancelReserve(userId, reserveInfo);
                            out.writeObject(result);
                            out.flush();
                        }
                        // 클라이언트 요청 - 기존 예약 정보를 새 예약 정보로 변경
                        if ("MODIFY_RESERVE".equals(command)) {
                            String userId = in.readUTF();
                            String oldReserveInfo = in.readUTF();
                            String newRoomNumber = in.readUTF();
                            String newDate = in.readUTF();
                            String newDay = in.readUTF();

                            // 1. 기존 예약 취소
                            ReserveResult cancelResult = ReserveManager.cancelReserve(userId, oldReserveInfo);
                            if (!cancelResult.getResult()) {
                                out.writeObject(cancelResult);
                                out.flush();
                                continue;
                            }
                            // 2. 새 예약 시도 (role은 기존 예약에서 추출하거나, 클라이언트에서 같이 보내도 됨)
                            // 여기서는 클라이언트에서 role도 같이 보내는 것이 안전하다고 판단
                            String giverole = in.readUTF();
                            ReserveResult reserveResult = ReserveManager.reserve(userId, giverole, newRoomNumber,
                                    newDate,
                                    newDay);
                            out.writeObject(reserveResult);
                            out.flush();
                        }
                        // 클라이언트 요청 - 예약 정보로 교수 예약 여부 조회 요청 받는 부분 - 교수 예약O true, 교수 예약X false
                        if ("FIND_PROFESSOR_BY_RESERVE".equals(command)) {
                            String reserveInfo = in.readUTF();
                            boolean found = ReserveManager.hasProfessorReserve(reserveInfo);
                            out.writeBoolean(found);
                            out.flush();
                       }

                        if ("SCHEDULE".equals(command)) {
                            System.out.println(">> [서버] SCHEDULE 명령 수신됨");
                            ScheduleRequest req = (ScheduleRequest) in.readObject();

                            ScheduleResult result;
                            TimeTableController controller = new TimeTableController();

                            switch (req.getCommand()) {
                                case "LOAD":
                                    Map<String, String> schedule = controller.getScheduleForRoom(
                                            req.getRoom(), req.getDay(), req.getType());
                                    result = new ScheduleResult(true, "조회 성공", schedule);
                                    break;

                                case "ADD":
                                    try {
                                        controller.addScheduleToFile(req.getRoom(), req.getDay(), req.getStart(), req.getEnd(), req.getSubject(), req.getType());
                                        result = new ScheduleResult(true, "등록 성공", null);
                                    } catch (Exception e) {
                                        result = new ScheduleResult(false, "등록 실패: " + e.getMessage(), null);
                                    }
                                    break;

                                case "DELETE":
                                    boolean deleted = controller.deleteScheduleFromFile(req.getRoom(), req.getDay(), req.getStart(), req.getEnd());
                                    result = new ScheduleResult(deleted, deleted ? "삭제 성공" : "삭제 실패", null);
                                    break;

                                case "UPDATE":
                                    boolean updated = controller.updateSchedule(req.getRoom(), req.getDay(), req.getStart(), req.getEnd(), req.getSubject(), req.getType());
                                    result = new ScheduleResult(updated, updated ? "수정 성공" : "수정 실패", null);
                                    break;

                                default:
                                    result = new ScheduleResult(false, "알 수 없는 명령입니다", null);
                            }

                            out.writeObject(result);
                            out.flush();
                        }

                    } catch (IOException e) {
                        System.out.println("Client Connection Error or Terminated. " + e.getMessage());
                        e.printStackTrace();
                        break;
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (acquired) {
                server.getConnectionLimiter().release();
            }

            if (id != null) {
                synchronized (server.getLoggedInUsers()) {
                    server.getLoggedInUsers().remove(id); // 로그아웃 처리
                }
            }

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } 
    /*
     * private void handleStudent(ObjectInputStream in, ObjectOutputStream out,
     * String id) {
     * System.out.println("학생 기능 처리: " + id);
     * }
     * 
     * private void handleProfessor(ObjectInputStream in, ObjectOutputStream out,
     * String id) {
     * System.out.println("교수 기능 처리: " + id);
     * }
     * 
     * private void handleAdmin(ObjectInputStream in, ObjectOutputStream out, String
     * id) {
     * System.out.println("관리자 기능 처리: " + id);
     * }
     */
}
