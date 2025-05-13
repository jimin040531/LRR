package deu.cse.lectureroomreservation2.client.view;

import java.awt.*;
import javax.swing.JOptionPane;

public class viewResultLR {
    
    public void viewResult() {
        boolean check = true;
        String reason = "성공 테스트";
        
        String showReason = "사유 : " + reason;
        if(check == false) {
            JOptionPane.showMessageDialog(null, "예약 실패!", showReason, JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "예약 성공!", null, JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    public void viewResult(boolean check, String reason) {
        String showReason = "사유 : " + reason;
        if(check == false) {
            JOptionPane.showMessageDialog(null, "예약 실패!", showReason, JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "예약 성공!", null, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
