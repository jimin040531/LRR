/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation.model;

import java.util.Map;

/**
 *
 * @author Prof.Jong Min Lee
 */
public class UserData {
    // for test. (Java 9부터 Map.of() 사용 가능)
    private Map<String, String> userData = Map.of(
            "admin", "admin",
            "tester1", "tester1",
            "tester2", "tester2"
            );
    
    /**
     * 사용자 ID에 해당하는 암호를 반환한다. 이런 식으로 암호를 plain text로
     * 취급하면 안 되므로 추후 수정해야 함.
     * @param userId 사용자 ID
     * @return 올바른 사용자 ID일 경우 암호를 반환하지만, 아닌 경우 ull을 반환한다.
     */
    public String getPassword(String userId) {
        String password = null;
        if (userData.containsKey(userId)) {
            password = userData.get(userId);
        }
        // System.out.printf("id = %s, password = %s%n%n", userId, password);
        return password;
    }
    
}
