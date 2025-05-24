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
/**
 * 사용자 정보를 .txt 파일 기반으로 관리 저장 형식: role,name,id,password (CSV)
 */
public class UserFileManager {

    // UserInfo.txt 파일 경로 설정
    private static final String filePath = System.getProperty("user.dir") + "/src/main/resources/UserInfo.txt";

    /**
     * 역할과 이름에 따라 사용자 검색
     *
     * @param roleFilter 역할 필터
     * @param nameFilter 이름 포함 문자열
     * @return 조건에 맞는 사용자 목록
     */
    public List<UserManage> searchUsers(String roleFilter, String nameFilter) {
        List<UserManage> result = new ArrayList<>();

        // 이름 필터가 비어있으면 전체 조회 방지
        if (nameFilter == null || nameFilter.trim().isEmpty()) {
            return result; // 아무 것도 반환하지 않음
        }

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

    /**
     * 사용자 1명을 파일에 추가 저장
     *
     * @param user 저장할 사용자 객체
     */
    public void saveUser(UserManage user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(String.join(",", user.getRole(), user.getName(), user.getId(), user.getPassword()));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 내부용: 전체 사용자 정보 덮어쓰기
     *
     * @param users 새로 저장할 전체 사용자 목록
     */
    private void overwriteAll(List<UserManage> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (UserManage user : users) {
                writer.write(String.join(",", user.getRole(), user.getName(), user.getId(), user.getPassword()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 사용자 삭제 삭제 후 나머지 사용자 목록을 파일에 다시 저장
     *
     * @param role 대상 역할
     * @param id 대상 ID
     * @return 삭제 성공 여부
     */
    public boolean deleteUser(String role, String id) {

        if (role.equals("A")) {
            return false;
        }

        File inputFile = new File("src/main/resources/UserInfo.txt");
        File tempFile = new File("src/main/resources/UserInfo_temp.txt");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile)); 
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", 4);  // 4까지만 분리하여 교수 수업정보 유지
                if (parts.length < 4) {
                    continue;
                }

                String currentRole = parts[0].trim();
                String currentId = parts[2].trim();

                // 삭제 대상이면 기록 X
                if (currentRole.equals(role) && currentId.equals(id)) {
                    continue;
                }

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return inputFile.delete() && tempFile.renameTo(inputFile);
    }

    /**
     * 파일에 저장된 모든 사용자 정보를 불러오기.
     *
     * @return 전체 사용자 목록
     */
    public List<UserManage> loadAllUsers() {
        List<UserManage> users = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 4) {
                    String role = tokens[0].trim();
                    String name = tokens[1].trim();
                    String id = tokens[2].trim();
                    String password = tokens[3].trim();

                    users.add(new UserManage(role, name, id, password));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }
}
