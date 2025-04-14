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
@Getter
@Setter
@Builder
public class LoginStatus {

    @Setter
    private Boolean loginSuccess;
    @Getter
    @Setter
    private String role = "NONE";

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // equals & hashCode 자동 생성됨
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Boolean.hashCode(this.loginSuccess);
        hash = 67 * hash + Objects.hashCode(this.role);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final LoginStatus other = (LoginStatus) obj;
        return this.loginSuccess == other.loginSuccess
                && Objects.equals(this.role, other.role);
    }

}
