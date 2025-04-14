/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import java.util.Objects;
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
    private String role = "NONE";
    
    public Boolean isLoginSuccess() {
        return loginSuccess;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.loginSuccess);
        hash = 67 * hash + Objects.hashCode(this.role);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LoginStatus other = (LoginStatus) obj;
        if (!Objects.equals(this.role, other.role)) {
            return false;
        }
        return Objects.equals(this.loginSuccess, other.loginSuccess);
    }

}
