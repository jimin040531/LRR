/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

/**
 *
 * @author H
 */
public class setReserveS {
    private String id;
    private String roomNumber;
    private String date;
    private String day;
    
    boolean result = false;
    String reason = "default";

    public setReserveS(String id, String roomNumber, String date, String day) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.date = date;
        this.day = day;
    }
    
    public boolean getResult() {
        
        return result;
    }
    
    public String getReason() {
        
        return reason;
    }
}
