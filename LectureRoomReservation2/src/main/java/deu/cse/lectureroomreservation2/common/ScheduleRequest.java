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

/**
 * 클라이언트가 서버에 시간표 관련 작업을 요청할 때 사용하는 클래스
 *
 * 주요 필드:
 * - command : 수행할 명령어 (ex : "LOAD", "ADD", "UPDATE", "DELETE")
 * - room : 강의실 번호 (예: "911", "912")
 * - day : 요일 정보 (예: "월", "화", ...)
 * - start : 시작 시간 (예: "09:00")
 * - end : 종료 시간 (예: "10:50")
 * - subject : 수업 일 때 -> 과목명, 제한일 때 -> 제한 사유
 * - type : 데이터 타입 (예: "수업", "제한")
 *
 * 사용 예: 서버에 특정 강의실의 수업 시간표 추가를 요청할 때 사용
 *
 */
public class ScheduleRequest implements Serializable {

    private String command;
    private String room;
    private String day;
    private String start;
    private String end;
    private String subject;
    private String type;

    // 생성자
    public ScheduleRequest(String command, String room, String day, String start, String end, String subject, String type) {
        this.command = command;
        this.room = room;
        this.day = day;
        this.start = start;
        this.end = end;
        this.subject = subject;
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public String getRoom() {
        return room;
    }

    public String getDay() {
        return day;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getSubject() {
        return subject;
    }

    public String getType() {
        return type;
    }

    // 객체의 필드 상태를 문자열로 출력
    @Override
    public String toString() {
        return "[command=" + command + ", room=" + room + ", day=" + day
                + ", start=" + start + ", end=" + end + ", subject=" + subject + ", type=" + type + "]";
    }
}
