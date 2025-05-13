/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.server.model.Professor;
import deu.cse.lectureroomreservation2.server.model.Student;
import deu.cse.lectureroomreservation2.server.model.UserManage;
import deu.cse.lectureroomreservation2.server.model.UserFileManager;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Jimin
 */
public class UserRequestController {
    private final UserFileManager FileManager = new UserFileManager();

    public List<String[]> handleSearchRequest(String roleFilter, String nameFilter) {
        List<UserManage> users = FileManager.searchUsers(roleFilter, nameFilter);
        List<String[]> result = new ArrayList<>();

        for (UserManage user : users) {
            result.add(new String[] {
                user.getRole(), user.getName(), user.getId(), user.getPassword()
            });
        }

        return result;
    }

    public void saveUser(String[] userData) {
        String role = userData[0];
        String name = userData[1];
        String id = userData[2];
        String password = userData[3];

        UserManage user = new UserManage(userData[0], userData[1], userData[2], userData[3]);
        if (role.equals("P")) {
            user = new Professor(name, id, password);
        } else {
            user = new Student(name, id, password);
        }

        FileManager.saveUser(user);
    }
    
    public List<String[]> saveUserAndGetUpdatedList(String[] userData) {
        saveUser(userData);
        return handleSearchRequest(userData[0], ""); // 같은 권한 전체 리스트 반환
    }
}