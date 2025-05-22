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

        UserManage user = new UserManage(role, name, id, password);  // 통합된 UserManage 사용
        FileManager.saveUser(user);
    }
    
    public List<String[]> saveUserAndGetUpdatedList(String[] userData) {
        saveUser(userData);
        return handleSearchRequest(userData[0], ""); // 같은 권한 전체 리스트 반환
    }

    public boolean deleteUser(String role, String id) {
        return FileManager.deleteUser(role, id);
    }

    public List<String[]> saveUserAndGetSingleUser(String[] newUser) {
        String role = newUser[0];
        String name = newUser[1];
        String id = newUser[2];
        String password = newUser[3];

        // 1. 현재 사용자 목록 불러오기
        List<UserManage> existingUsers = FileManager.searchUsers(role, ""); // 해당 역할 전체 조회

        // 2. ID 중복 검사
        for (UserManage user : existingUsers) {
            if (user.getId().equals(id)) {
                throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
            }
        }

        // 3. 중복 아니면 저장
        UserManage user = new UserManage(role, name, id, password);
        FileManager.saveUser(user);  // 반환값 신경 안 써도 됨

        // 4. 새 사용자 1명만 포함된 리스트 반환
        List<String[]> result = new ArrayList<>();
        result.add(new String[] { role, name, id, password });
        return result;
    }
}