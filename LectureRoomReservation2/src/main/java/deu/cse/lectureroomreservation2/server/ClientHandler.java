/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server;

/**
 *
 * @author SAMSUNG
 */
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

            id = in.readUTF();
            String password = in.readUTF();
            String role = in.readUTF();

            acquired = server.getConnectionLimiter().tryAcquire();
            if (!acquired) {
                System.out.println("접속 거부됨 (최대 인원 초과): " + id);
                out.writeObject(new LoginStatus(false, "WAIT", "현재 접속 인원이 가득 찼습니다."));
                out.flush();
                return;
            }

            // 1. 인증 먼저
            LoginStatus status = server.requestAuth(id, password, role);

            // 2. 인증 성공한 경우에만 중복 로그인 체크
            if (status.isLoginSuccess()) {
                synchronized (server.getLoggedInUsers()) {
                    if (server.isUserLoggedIn(id)) {
                        System.out.println("접속 거부(중복 로그인): " + id);
                        out.writeObject(new LoginStatus(false, "DUPLICATE", "이미 로그인 중인 계정입니다."));
                        out.flush();
                        return;
                    } else {
                        server.addLoggedInUser(id, socket);  // ✅ 인증 완료 후에만 등록
                    }
                }
            }

            // 3. 로그인 결과 전송
            out.writeObject(status);
            out.flush();

            // 4. 로그인 성공한 경우 명령 수신 루프
            if (status.isLoginSuccess()) {
                while (true) {
                    try {
                        String command = in.readUTF();
                        if ("LOGOUT".equalsIgnoreCase(command)) {
                            System.out.println("사용자 로그아웃됨: " + id);
                            break;
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

            // 세션 종료 후 항상 상태 정리
            if (id != null) {
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
}
