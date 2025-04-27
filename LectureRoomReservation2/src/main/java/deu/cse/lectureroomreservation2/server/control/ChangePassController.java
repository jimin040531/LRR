/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

import deu.cse.lectureroomreservation2.server.model.UserData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SAMSUNG
 */
public class ChangePassController {

    private UserData userData;

    public ChangePassController() {
        userData = new UserData();
    }

    /**
     * 비밀번호 변경을 시도하는 메서드
     *
     * @param userId 변경할 사용자 ID
     * @param currentPw 현재 비밀번호
     * @param newPw 새 비밀번호
     * @return 비밀번호 변경 성공 시 true, 실패 시 false
     */
    public boolean changePassword(String userId, String currentPw, String newPw) {
        if (userId == null || currentPw == null || newPw == null) {
            return false;
        }
        return userData.updatePassword(userId, currentPw, newPw);
    }

    /**
     * user.txt 파일을 읽어 해당 userId의 비밀번호를 새 비밀번호로 변경한다.
     *
     * @param userId 사용자 ID
     * @param newPw 새 비밀번호
     * @return 파일 업데이트 성공 시 true, 실패 시 false
     */
    private boolean updatePasswordInFile(String userId, String newPw) {
        File file = new File("user.txt");
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\\t"); // 탭으로 구분
                if (tokens.length == 3) {
                    String fileId = tokens[0].trim();
                    String filePw = tokens[1].trim();
                    String fileRole = tokens[2].trim();

                    if (fileId.equals(userId)) {
                        // 해당 아이디 발견: 새 비밀번호로 교체
                        line = fileId + "\t" + newPw + "\t" + fileRole;
                    }
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // 읽은 내용을 다시 파일에 덮어쓰기
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
