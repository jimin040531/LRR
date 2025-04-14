/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

//import deu.cse.lectureroomreservation.view.Login;
import deu.cse.lectureroomreservation2.server.model.UserData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author skylo
 */
public class LoginController {

    //private UserData userData = new UserData(); // 향후 확장용
    private Map<String, Boolean> loginStatus = new HashMap<>();

    public LoginStatus authenticate(String id, String password, String selectedRole) {
        LoginStatus status = new LoginStatus();
        status.setLoginSuccess(false);
        status.setRole("NONE");

        if (id == null || id.trim().isEmpty()
                || password == null || password.trim().isEmpty()
                || selectedRole == null || selectedRole.trim().isEmpty()) {
            return status;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("user.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split("\t");

                if (tokens.length != 3) {
                    continue;
                }

                String fileId = tokens[0].trim();
                String filePw = tokens[1].trim();
                String fileRole = tokens[2].trim().toUpperCase();

                if (fileId.equals(id) && filePw.equals(password) && fileRole.equals(selectedRole.toUpperCase())) {

                    boolean valid = false;
                    switch (fileRole) {
                        case "STUDENT":
                            valid = id.length() == 8 && id.matches("\\d{8}");
                            break;
                        case "PROFESSOR":
                            valid = id.length() == 5 && id.matches("\\d{5}");
                            break;
                        case "ADMIN":
                            valid = true;
                            break;
                        default:
                            return status;
                    }

                    if (!valid) {
                        return status;
                    }

                    status.setLoginSuccess(true);
                    status.setRole(fileRole);
                    return status;
                }
            }

        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        }

        return status;
    }
}
