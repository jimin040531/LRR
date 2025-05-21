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
        boolean isLoggedIn = false;

        try {
            System.out.println("클라이언트 연결 요청 수신됨: " + socket.getInetAddress());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            id = in.readUTF();
            String password = in.readUTF();
            String role = in.readUTF();

            // 최대 동시 접속 수 확인
            acquired = server.getConnectionLimiter().tryAcquire();
            if (!acquired) {
                out.writeObject(new LoginStatus(false, "WAIT", "현재 접속 인원이 가득 찼습니다."));
                out.flush();
                return;
            }

            // 중복 로그인 확인
            synchronized (server.getLoggedInUsers()) {
                if (server.isUserLoggedIn(id)) {
                    out.writeObject(new LoginStatus(false, "DUPLICATE", "이미 로그인 중인 계정입니다."));
                    out.flush();
                    return;
                }
            }

            // 인증 처리
            LoginStatus status = server.requestAuth(id, password, role);
            if (status.isLoginSuccess()) {
                synchronized (server.getLoggedInUsers()) {
                    if(server.isUserLoggedIn(id)){
                        System.out.println("중복 로그인 : "+id);
                        out.writeObject(new LoginStatus(false,"DUPLICATE","이미 로그인 중인 계정입니다."));
                        out.flush();
                        return;
                    }else{
                        server.addLoggedInUser(id);
                    }
                }
            }

            out.writeObject(status);
            out.flush();

            // 로그인 성공 시 명령 루프 시작
            if (status.isLoginSuccess()) {
                // 공지사항 처리 (학생용)
                if ("STUDENT".equals(status.getRole())) {
                    List<String> notices = noticeController.getNotices(id);
                    for (String notice : notices) {
                        out.writeUTF("NOTICE");
                        out.writeUTF(notice);
                        out.flush();
                        noticeController.removeNotice(id, notice);
                    }
                    out.writeUTF("NOTICE_END");
                    out.flush();
                }

                // 명령 루프
                while (true) {
                    try {
                        String command = in.readUTF();
                        if ("LOGOUT".equalsIgnoreCase(command)) {
                            System.out.println("사용자 로그아웃됨: " + id);
                            break;
                        }

                        handleClientCommand(command, in, out, id);
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
            if (id != null && isLoggedIn) {
                synchronized (server.getLoggedInUsers()) {
                    server.removeLoggedInUser(id);
                }
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleClientCommand(String command, ObjectInputStream in, ObjectOutputStream out, String id) throws IOException, ClassNotFoundException {
        switch (command) {
            case "RESERVE":
                ReserveRequest req = (ReserveRequest) in.readObject();
                ReserveResult result = new receiveController().handleReserve(req);
                out.writeObject(result);
                out.flush();
                break;

            case "CHECK_MAX_TIME":
                CheckMaxTimeRequest maxReq = (CheckMaxTimeRequest) in.readObject();
                boolean exceeded = new CheckMaxTime(maxReq.getId()).check();
                out.writeObject(new CheckMaxTimeResult(exceeded, exceeded ? "최대 예약 개수 초과" : "예약 가능"));
                out.flush();
                break;

            case "RETRIEVE_MY_RESERVE":
                List<String> reserves = ReserveManager.getReserveInfoById(in.readUTF());
                out.writeObject(reserves);
                out.flush();
                break;

            case "COUNT_RESERVE_USERS":
                int count = ReserveManager.countUsersByReserveInfo(in.readUTF());
                out.writeInt(count);
                out.flush();
                break;

            case "CANCEL_RESERVE":
                String userId = in.readUTF();
                String reserveInfo = in.readUTF();
                ReserveResult cancelResult = ReserveManager.cancelReserve(userId, reserveInfo);
                out.writeObject(cancelResult);
                out.flush();
                break;

            case "MODIFY_RESERVE":
                handleModifyReserve(in, out);
                break;
        }
    }

    private void handleModifyReserve(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        try {
            String userId = in.readUTF();
            String oldReserveInfo = in.readUTF();
            String newRoomNumber = in.readUTF();
            String newDate = in.readUTF();
            String newDay = in.readUTF();
            String role = in.readUTF();

            ReserveResult cancelResult = ReserveManager.cancelReserve(userId, oldReserveInfo);
            if (!cancelResult.getResult()) {
                out.writeObject(cancelResult);
                out.flush();
                return;
            }

            ReserveResult reserveResult = ReserveManager.reserve(userId, role, newRoomNumber, newDate, newDay);
            out.writeObject(reserveResult);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
