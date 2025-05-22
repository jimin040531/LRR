/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.common;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Jimin
 */
public class UserResult implements Serializable {
    private boolean success;
    private String message;
    private List<String[]> data;

    public UserResult(boolean success, String message, List<String[]> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<String[]> getData() {
        return data;
    }
    
    public List<String[]> getUserList() {
        return data;
    }
}