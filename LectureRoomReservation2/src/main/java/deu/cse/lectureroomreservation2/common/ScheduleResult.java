/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.common;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Jimin
 */
public class ScheduleResult implements Serializable {
    private boolean success;
    private String message;
    private Map<String, String> data; // 시간:과목명

    public ScheduleResult(boolean success, String message, Map<String, String> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Map<String, String> getData() { return data; }
}
