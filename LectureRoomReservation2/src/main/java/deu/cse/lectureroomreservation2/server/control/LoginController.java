/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

// import deu.cse.lectureroomreservation.view.Login;
import deu.cse.lectureroomreservation2.server.model.UserData;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author skylo
 */
public class LoginController {

    private UserData userData = new UserData();
    private Map<String, Boolean> loginStatus = new HashMap<>();


    public LoginStatus authenticate(String id, String password) {
        LoginStatus status = LoginStatus.builder()
                .loginSuccess(false)
                .role("NONE") // "USER", "MANAGER", "ADMIN"
                .build();

        if (id == null || "".equals(id)) {
            return status;
        }
        if (password == null || "".equals(password)) {
            return status;
        }
        if (password.equals(userData.getPassword(id))) {
            status.setLoginSuccess(true);
            if ("admin".equals(id)) {
                status.setRole("ADMIN");
            } else {
                status.setRole("USER");
            }
        }
        return status;
    }
}
