/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.client;

/**
 * 처음부터 arraylist쓸걸...
 *
 */
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;
import java.awt.Color;
import java.awt.Component;

import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import deu.cse.lectureroomreservation2.server.model.GetReservation;
import deu.cse.lectureroomreservation2.client.view.*;

import javax.swing.table.DefaultTableModel;  // JTable 모델 관련
import java.util.List;                       // List 사용
import java.util.ArrayList;                 // ArrayList 사용
import java.util.Arrays;                    // Arrays.copyOf 등 사용


public abstract class User {

    protected String Name;          //이름
    protected String PhoneNumber;   //휴대폰 번호 -> 비번에 사용
    protected int ReservationTime;  //예약 시간
    protected String Id;            //아이디 = 학번/교수번호/관리자는 마음대로
    protected String PassWord;      //초기 비번 = 휴대폰 번호 뒷8자리

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public int getReservationTime() {
        return ReservationTime;
    }

    public void setReservationTime(int ReservationTime) {
        this.ReservationTime = ReservationTime;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String PassWord) {
        this.PassWord = PassWord;
    }

    public List<String[]> getMyReservation(String userID) {
        String[][] ReservationInfo = GetReservation.GetTime("reservation");
        List<String[]> MyReservations = new ArrayList<>();

        for (String[] row : ReservationInfo) {
            if (row[5].equals(userID)) {
                MyReservations.add(Arrays.copyOf(row, row.length)); // 동적으로 리스트에 추가
            }
        }

        for (String[] row : MyReservations) {
            System.out.println(userID + "님의 예약:");
            System.out.println(Arrays.toString(row));
        }
        
        return MyReservations;
    }

    public void myInsertTable(List<String[]> MyReservations,JTable MyReservationTable) {
        // JTable 모델 가져오기
        DefaultTableModel model = (DefaultTableModel) MyReservationTable.getModel();
        model.setRowCount(0); // 기존 데이터 초기화

        // JTable에 MyReservations 추가
        for (String[] row : MyReservations) {
            String startTime = row[2];
            String endTime = row[3];
            String room = row[0];
            String day = row[1];

            model.addRow(new Object[]{startTime, endTime, room, day});
        }
    }
    
    public String[] DeleteReservation(String UserID, String[][] ReservationInfo,String[][] MyReservation){
    String[] deleteReservation = {" "," "," "," ","1",UserID," "}; //908,월,11:00,11:50,1,20230001,25/05/30
    
    
    
        
    
    
    
    return deleteReservation;
    }
    
}
