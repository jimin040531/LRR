/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation.control;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Prof.Jong Min Lee
 */
@Builder
public class LoginStatus {
    @Setter
    private Boolean loginSuccess;
    @Getter @Setter
    private String role;
    
    public Boolean isLoginSuccess() {
        return loginSuccess;
    }
}
