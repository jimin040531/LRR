/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package deu.cse.lectureroomreservation2.client;

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
