/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation.control;

import deu.cse.lectureroomreservation.model.UserData;
import deu.cse.lectureroomreservation.view.Login;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author skylo
 */
public class LoginController {

    private Login login = new Login();
    private UserData userData = new UserData();
    private Map<String, Boolean> loginStatus = new HashMap<>();

    public LoginStatus processLogin() {
        // 로그인 화면 보여주기
        // Login login = new Login();
        login.showLoginView();
        // 인증
        LoginStatus loginStatus = authenticate(login.getId(), login.getPassword());
        // 결과 반환
        return loginStatus;
    }

    private LoginStatus authenticate(String id, String password) {
        LoginStatus status = LoginStatus.builder()
                .loginSuccess(false)
                .role("USER") // "USER", "MANAGER", "ADMIN"
                .build();

        if (id == null || "".equals(id)) {
            return status;
        }
        if (password == null || "".equals(password)) {
            return status;
        }
        if (password.equals(userData.getPassword(id))) {
            status.setLoginSuccess(true);
            if ("admin".equals("id")) {
                status.setRole("ADMIN");
            }
        }
        return status;
    }
}
