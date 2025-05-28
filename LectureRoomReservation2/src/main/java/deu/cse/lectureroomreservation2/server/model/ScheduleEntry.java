/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.model;

/**
 *
 * @author Jimin
 */

/**
 * 시간표의 한 항목(수업 또는 제한)을 나타내는 클래스
 * 과목명, 시작/종료 시간, 유형 정보를 포함
 */
public class ScheduleEntry {
    private String subject;
    private String type;       // "수업" 또는 "제한"
    private String startTime;
    private String endTime;

    public ScheduleEntry(String subject, String type, String startTime, String endTime) {
        this.subject = subject;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getSubject() {
        return subject;
    }

    public String getType() {
        return type;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
