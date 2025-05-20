/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import java.io.*;
import deu.cse.lectureroomreservation2.server.control.*;

/**
 *
 * @author H
 */
public class CheckMaxTime implements Serializable {
    private String id;

    //private final String USER_FILE = "UserInfotest.txt";
    private final String USER_FILE = receiveController.getFilepath() + "UserInfotest.txt";
    
    public CheckMaxTime(String id) {
        this.id = id;
    }

    // 예약 정보가 4개인지 확인
    public boolean check() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(id)) {
                    // 예약 정보는 6번째부터
                    int reserveCount = parts.length - 5;
                    return reserveCount == 4;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // id를 찾지 못하면 false
    }
}
