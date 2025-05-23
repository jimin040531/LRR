/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.common;

import java.io.Serializable;

/**
 *
 * @author Jimin
 */
public class ReserveManageRequest implements Serializable {

    private final String command;       // "SEARCH", "UPDATE", "DELETE" 등 명령
    private final String userId;        // 사용자 ID
    private final String room;          // 강의실 번호 (검색 시)
    private final String date;          // 날짜 (검색 시)
    private final String oldReserveInfo; // 기존 예약 정보 (수정 시)
    private final String newRoom;       // 새 강의실 번호 (수정 시)
    private final String newDate;       // 새 날짜 (수정 시)
    private final String newDay;        // 새 요일 (수정 시)
    private final String role;          // 사용자 역할 (필요 시)
    private final String reserveInfo;   // 예약 정보 (삭제 시)

    public ReserveManageRequest(String command, String userId, String room, String date,
            String oldReserveInfo, String newRoom, String newDate,
            String newDay, String role, String reserveInfo) {
        this.command = command;
        this.userId = userId;
        this.room = room;
        this.date = date;
        this.oldReserveInfo = oldReserveInfo;
        this.newRoom = newRoom;
        this.newDate = newDate;
        this.newDay = newDay;
        this.role = role;
        this.reserveInfo = reserveInfo;
    }

    public String getCommand() {
        return command;
    }

    public String getUserId() {
        return userId;
    }

    public String getRoom() {
        return room;
    }

    public String getDate() {
        return date;
    }

    public String getOldReserveInfo() {
        return oldReserveInfo;
    }

    public String getNewRoom() {
        return newRoom;
    }

    public String getNewDate() {
        return newDate;
    }

    public String getNewDay() {
        return newDay;
    }

    public String getRole() {
        return role;
    }

    public String getReserveInfo() {
        return reserveInfo;
    }
}
