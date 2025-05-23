/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.model;

import java.io.*;
import java.util.*;

/**
 *
 * @author Jimin
 */
public class UserFileManager {

    private static final String filePath = System.getProperty("user.dir") + "/src/main/resources/UserInfo.txt";

    public List<UserManage> searchUsers(String roleFilter, String nameFilter) {
    List<UserManage> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 5);  // 최대 5개까지만 자름 (예약정보까지 포함 가능)

                if (parts.length < 4) {
                    continue;
                }

                String role = parts[0].trim();
                String name = parts[1].trim();
                String id = parts[2].trim();
                String password = parts[3].trim();

                // 필터 조건에 맞으면 추가
                if (role.equals(roleFilter) && name.contains(nameFilter)) {
                    result.add(new UserManage(role, name, id, password));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void saveUser(UserManage user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(String.join(",", user.getRole(), user.getName(), user.getId(), user.getPassword()));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void overwriteAll(List<UserManage> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (UserManage user : users) {
                writer.write(String.join(",", user.getRole(), user.getName(), user.getId(), user.getPassword()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isIdDuplicate(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    continue;
                }

                String existingId = parts[2].trim();
                if (existingId.equals(id)) {
                    return true;  // 중복 발견
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;  // 중복 없음
    }

    public boolean deleteUser(String role, String id) {
        List<UserManage> allUsers = new ArrayList<>();

        // 파일 전체 읽기
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 4) continue;

                String fileRole = parts[0].trim();
                String name = parts[1].trim();
                String fileId = parts[2].trim();
                String password = parts[3].trim();

                // 삭제할 조건이 아니면 리스트에 유지
                if (!(fileRole.equals(role) && fileId.equals(id))) {
                    allUsers.add(new UserManage(fileRole, name, fileId, password));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // 삭제 여부 판단
        boolean removed = true; // 파일에서 일치하는 걸 제거했으므로 제거 성공

        // 다시 저장
        overwriteAll(allUsers);

        return removed;
    }
}
