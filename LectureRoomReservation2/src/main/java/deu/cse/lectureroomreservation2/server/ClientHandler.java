/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server;

/**
 *
 * @author SAMSUNG
 */
import deu.cse.lectureroomreservation2.server.control.*;
import deu.cse.lectureroomreservation2.common.*;
import java.util.List;
import deu.cse.lectureroomreservation2.server.control.LoginStatus;
import java.io.*;
import java.net.Socket;

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
            System.out.println("클라이언트 연결 요청 수신됨: " + socket.getInetAddress());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // 사용자 정보 먼저 받음
            id = in.readUTF();
            String password = in.readUTF();
            String role = in.readUTF();

            // 세마포어 체크는 로그인 정보 받고 나서 수행
            acquired = server.getConnectionLimiter().tryAcquire();
            if (!acquired) {
                System.out.println("접속 거부됨 (최대 인원 초과): " + id);
                out.writeObject(new LoginStatus(false, "WAIT", "현재 접속 인원이 가득 찼습니다. 잠시 후 다시 시도해 주세요."));
                out.flush();
                return;
            }

            // 중복로그인 체크
            synchronized (server.getLoggedInUsers()) {
                if (server.getLoggedInUsers().contains(id)) {
                    System.out.println("접속 거부(중복 로그인): " + id);
                    out.writeObject(new LoginStatus(false, "DUPLICATE", "이미 로그인 중인 계정입니다."));
                    out.flush();
                    return;
                }
            }

            LoginStatus status = server.requestAuth(id, password, role);    // 인증
            if (status.isLoginSuccess()) {
                synchronized (server.getLoggedInUsers()) {
                    server.getLoggedInUsers().add(id);  // 로그인 성공한 사용자 등록
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
                            System.out.println("사용자 로그아웃됨: " + id);
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
                    } catch (IOException e) {
                        System.out.println("클라이언트 연결 오류 또는 종료됨.");
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
                    server.getLoggedInUsers().remove(id);  // 로그아웃 처리
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
    private void handleStudent(ObjectInputStream in, ObjectOutputStream out, String id) {
        System.out.println("학생 기능 처리: " + id);
    }

    private void handleProfessor(ObjectInputStream in, ObjectOutputStream out, String id) {
        System.out.println("교수 기능 처리: " + id);
    }

    private void handleAdmin(ObjectInputStream in, ObjectOutputStream out, String id) {
        System.out.println("관리자 기능 처리: " + id);
    }*/
}
