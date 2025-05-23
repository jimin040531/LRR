/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.model;

/**
 *
 * @author Jimin
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
