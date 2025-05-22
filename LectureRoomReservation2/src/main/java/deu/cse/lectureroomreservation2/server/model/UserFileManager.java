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
                String[] parts = line.split(",");
                if (parts.length != 4) continue;

                String role = parts[0].trim();
                String name = parts[1].trim();
                String id = parts[2].trim();
                String password = parts[3].trim();

                if (role.equals(roleFilter) && name.contains(nameFilter)) {
                    result.add(new UserManage(role, name, id, password));  // UserManage 사용
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
                if (parts.length != 4) continue;

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
}
