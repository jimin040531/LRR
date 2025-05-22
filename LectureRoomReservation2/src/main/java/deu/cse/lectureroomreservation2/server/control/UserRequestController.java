/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.common.UserRequest;
import deu.cse.lectureroomreservation2.common.UserResult;
import deu.cse.lectureroomreservation2.server.model.UserManage;
import deu.cse.lectureroomreservation2.server.model.UserFileManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jimin
 */
public class UserRequestController {

    private final UserFileManager fileManager = new UserFileManager();

    // 사용자 검색 처리
    public UserResult handleSearch(UserRequest request) {
        List<UserManage> users = fileManager.searchUsers(request.getRole(), request.getName());
        List<String[]> data = new ArrayList<>();
        for (UserManage user : users) {
            data.add(new String[]{
                user.getRole(), user.getName(), user.getId(), user.getPassword()
            });
        }
        return new UserResult(true, "조회 성공", data);
    }

    // 사용자 등록 처리
    public UserResult handleAdd(UserRequest request) {
        if (fileManager.isIdDuplicate(request.getId())) {
            return new UserResult(false, "이미 존재하는 아이디입니다.", null);
        }

        UserManage newUser = new UserManage(
                request.getRole(), request.getName(), request.getId(), request.getPassword()
        );
        fileManager.saveUser(newUser);

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{
            newUser.getRole(), newUser.getName(), newUser.getId(), newUser.getPassword()
        });

        return new UserResult(true, "등록 성공", data);
    }

    // 사용자 삭제 처리
    public UserResult handleDelete(UserRequest request) {
        boolean removed = fileManager.deleteUser(request.getRole(), request.getId());
        String message = removed ? "삭제 성공" : "삭제 실패: 사용자를 찾을 수 없음";
        return new UserResult(removed, message, null);
    }

}
