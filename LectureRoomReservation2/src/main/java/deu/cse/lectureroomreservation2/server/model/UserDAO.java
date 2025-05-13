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
public class UserDAO {
    private static final String filePath = System.getProperty("user.dir") + "/src/main/resources/UserInfo.txt";

    public List<User> searchUsers(String roleFilter, String nameFilter) {
        List<User> result = new ArrayList<>();

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
                    if (role.equals("P")) {
                        result.add(new Professor(name, id, password));
                    } else if (role.equals("S")) {
                        result.add(new Student(name, id, password));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(String.join(",", user.getRole(), user.getName(), user.getId(), user.getPassword()));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
