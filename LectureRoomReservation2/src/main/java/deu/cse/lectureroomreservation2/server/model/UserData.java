/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.model;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 *
 * @author Prof.Jong Min Lee
 */
public class UserData {

    private final String USER_FILE = "UserInfo.txt";

    /**
     * 사용자 ID에 해당하는 암호를 반환한다. 이런 식으로 암호를 plain text로 취급하면 안 되므로 추후 수정해야 함.
     *
     * @param userId 사용자 ID
     * @return 올바른 사용자 ID일 경우 암호를 반환하지만, 아닌 경우 ull을 반환한다.
     */
    public Optional<User> getUser(String id, String password, String role) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(USER_FILE); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split(",");
                /* if (tokens.length != 4) {
                    continue;
                }*/

                //  UserInfo.txt 기준 (Role, 이름 , ID, PW)
                String fileRole = tokens[0].trim().toUpperCase(); // 역할 (S, P, A)
                String fileId = tokens[2].trim();               // ID
                String filePw = tokens[3].trim();               // Password

                if (!(fileRole.equals("S") || fileRole.equals("P") || fileRole.equals("A"))) {
                    continue;
                }

                if (fileId.equals(id.trim())
                        && filePw.equals(password.trim())
                        && fileRole.equals(role.trim().toUpperCase())) {
                    return Optional.of(new User(fileId, filePw, fileRole));
                }
            }

        } catch (IOException | NullPointerException e) {
            System.err.println("userInfo.txt 파일 읽기 오류: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * 주어진 ID와 현재 비밀번호가 일치하면 새 비밀번호로 업데이트
     *
     * @param id 사용자 ID
     * @param currentPw 현재 비밀번호
     * @param newPw 새 비밀번호
     * @return 변경 성공 시 true, 실패 시 false
     */
    public boolean updatePassword(String id, String currentPw, String newPw) {
        try {
            URL resourceUrl = getClass().getClassLoader().getResource(USER_FILE);
            if (resourceUrl == null) {
                System.err.println("userinfo.txt 파일 경로를 찾을 수 없습니다.");
                return false;
            }

            File file = new File(resourceUrl.toURI());
            List<String> lines = new ArrayList<>();
            boolean updated = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");

                    if (parts.length == 4) {
                        String fileId = parts[2].trim();
                        String filePW = parts[3].trim();

                        if (fileId.equals(id) && filePW.equals(currentPw)) {
                            parts[3] = newPw;
                            updated = true;
                        }

                        lines.add(String.join(",", parts));
                    } else {
                        lines.add(line);
                    }
                }
            }

            if (updated) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (String l : lines) {
                        writer.write(l);
                        writer.newLine();
                    }
                }
            }

            return updated;

        } catch (Exception e) {
            System.err.println("비밀번호 변경 오류: " + e.getMessage());
            return false;
        }
    }
}
