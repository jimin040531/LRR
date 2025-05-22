/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

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
        
        // 아이디 중복 검사
        if (FileManager.isIdDuplicate(id)) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        UserManage user = new UserManage(role, name, id, password);  // 통합된 UserManage 사용
        FileManager.saveUser(user);
    }
    
    public List<String[]> saveUserAndGetSingleUser(String[] userData) {
        saveUser(userData);
        List<String[]> singleUserList = new ArrayList<>();
        singleUserList.add(userData);
        return singleUserList;
    }

    public boolean deleteUser(String role, String id) {
        List<UserManage> users = FileManager.searchUsers(role, ""); // 해당 권한 모든 사용자 불러오기
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            FileManager.overwriteAll(users); // 변경 사항 저장
        }
        return removed;
    }
}