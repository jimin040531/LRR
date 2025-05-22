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

    public String getCommand() { return command; }
    public String getRoom() { return room; }
    public String getDay() { return day; }
    public String getStart() { return start; }
    public String getEnd() { return end; }
    public String getSubject() { return subject; }
    public String getType() { return type; }
    
    @Override
public String toString() {
    return "[command=" + command + ", room=" + room + ", day=" + day + 
           ", start=" + start + ", end=" + end + ", subject=" + subject + ", type=" + type + "]";
}
}