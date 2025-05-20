/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package deu.cse.lectureroomreservation2.client;

import deu.cse.lectureroomreservation2.common.CheckMaxTimeRequest;
import deu.cse.lectureroomreservation2.common.CheckMaxTimeResult;
import deu.cse.lectureroomreservation2.common.ReserveRequest;
import deu.cse.lectureroomreservation2.common.ReserveResult;
import deu.cse.lectureroomreservation2.server.control.LoginStatus;
import java.io.*;
import java.net.Socket;

/**
 * Client는 강의실 예약 시스템에서 뷰와 관련된 행위를 다룬다. 필요시 Server에 요청하여 필요한 작업을 수행할 수 있다.
 *
 * @author Prof.Jong Min Lee
 */
public class Client {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private LoginStatus status;

    public Client(String serverAddress, int serverPort) {
        try {
            this.socket = new Socket(serverAddress, serverPort);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println(" 서버 연결 실패: " + e.getMessage());
        }
    }

    public void sendLoginRequest(String id, String password, String role) throws IOException {
        out.writeUTF(id);
        out.writeUTF(password);
        out.writeUTF(role);
        out.flush();
    }

    public LoginStatus receiveLoginStatus() throws IOException, ClassNotFoundException {
        status = (LoginStatus) in.readObject();
        return status;
    }

    public void logout() {
        try {
            out.writeUTF("LOGOUT");
            out.flush();
            socket.close();
        } catch (IOException e) {
            System.err.println(" 로그아웃 중 오류 발생: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    // 예약 요청 처리
    public ReserveResult sendReserveRequest(String id, String role, String roomNumber, String date, String day, String notice)
            throws IOException, ClassNotFoundException {
        // 예약 요청 객체 생성
        ReserveRequest req = new ReserveRequest(id, role, roomNumber, date, day, notice);
        // 서버에 예약 명령 전송
        out.writeUTF("RESERVE");
        out.flush();
        out.writeObject(req);
        out.flush();
        // 서버로부터 결과 수신
        return (ReserveResult) in.readObject();
    }
    
    // 최대 예약 시간 체크 요청 처리
    public CheckMaxTimeResult sendCheckMaxTimeRequest(String id) throws IOException, ClassNotFoundException {
        out.writeUTF("CHECK_MAX_TIME");
        out.flush();
        out.writeObject(new CheckMaxTimeRequest(id));
        out.flush();
        return (CheckMaxTimeResult) in.readObject();
    }

    public static void main(String[] args) {
        try {
            Client c = new Client("localhost", 5000);
            if (c.isConnected()) {
                LoginStatus status = c.receiveLoginStatus();
                c.logout();
            }
            else{
                System.err.println("서버에 연결되지 않았습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
