/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package deu.cse.lectureroomreservation2.client.view;

import deu.cse.lectureroomreservation2.client.Client;

/**
 *
 * @author User
 */
public class StudentMainMenu extends javax.swing.JFrame {

    /**
     * Creates new form StudentMainMenu
     */
    private final String userId;
    private final Client client;

    public StudentMainMenu(String userId, Client client) {
        this.userId = userId;
        this.client = client;

        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(330, 465);
        setLocationRelativeTo(null);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (client != null) {
                    client.logout(); // 서버에 로그아웃 알리기
                }
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stu_logout = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        reserv_time_check = new javax.swing.JButton();
        stu_pass_change = new javax.swing.JButton();
        quick_booking = new javax.swing.JButton();
        reserv_check1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        stu_logout.setText("로그아웃");
        stu_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stu_logoutActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("맑은 고딕", 1, 24)); // NOI18N
        jLabel1.setText("Student Main");
        jLabel1.setToolTipText("");

        reserv_time_check.setFont(new java.awt.Font("맑은 고딕", 1, 18)); // NOI18N
        reserv_time_check.setText("강의실 조회 및 예약");
        reserv_time_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reserv_time_checkActionPerformed(evt);
            }
        });

        stu_pass_change.setText("비밀번호 변경");
        stu_pass_change.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stu_pass_changeActionPerformed(evt);
            }
        });

        quick_booking.setFont(new java.awt.Font("맑은 고딕", 1, 18)); // NOI18N
        quick_booking.setText("빠른 예약");
        quick_booking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quick_bookingActionPerformed(evt);
            }
        });

        reserv_check1.setFont(new java.awt.Font("맑은 고딕", 1, 18)); // NOI18N
        reserv_check1.setText("예약 확인");
        reserv_check1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reserv_check1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(stu_pass_change)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(stu_logout, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel1))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(37, 37, 37)
                                    .addComponent(reserv_time_check, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(quick_booking, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                                    .addComponent(reserv_check1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(1, 1, 1)))
                        .addGap(0, 27, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(reserv_time_check, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(quick_booking, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(reserv_check1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stu_logout, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stu_pass_change, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void stu_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stu_logoutActionPerformed
        // TODO add your handling code here:
        int choice = javax.swing.JOptionPane.showConfirmDialog(this, "로그아웃 하시겠습니까?", "로그아웃", javax.swing.JOptionPane.YES_NO_OPTION);

        if (choice == javax.swing.JOptionPane.YES_OPTION) {
            if (client != null) {
                client.logout();
            }
            this.dispose();  // 현재 창 닫기
            new LoginFrame().setVisible(true); // 로그인 화면 띄우기
        }
    }//GEN-LAST:event_stu_logoutActionPerformed

    private void stu_pass_changeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stu_pass_changeActionPerformed
        // TODO add your handling code here:
        ChangePassView frame = new ChangePassView();

        // 현재 클라이언트의 스트림 전달
        frame.setStreams(client.getOutputStream(), client.getInputStream());

        // 현재 로그인된 사용자 ID 전달
        frame.setUserId(userId);
        frame.setSize(300, 450);           
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);   
    }//GEN-LAST:event_stu_pass_changeActionPerformed

    private void quick_bookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quick_bookingActionPerformed
        // TODO add your handling code here:
        QuickViewRoom qvroom = new QuickViewRoom(client, userId, "S");
        qvroom.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_quick_bookingActionPerformed

    private void reserv_check1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reserv_check1ActionPerformed
        // TODO add your handling code here:
        MyReservationView myreserveview = new MyReservationView(client, userId, "S");
        myreserveview.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_reserv_check1ActionPerformed

    private void reserv_time_checkActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        // TODO add your handling code here:
        ViewRoom viewroom = new ViewRoom(client, userId, "S", null);
        viewroom.setVisible(true);
        this.dispose();
    }                                             

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StudentMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StudentMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StudentMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StudentMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StudentMainMenu("S", null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton quick_booking;
    private javax.swing.JButton reserv_check1;
    private javax.swing.JButton reserv_time_check;
    private javax.swing.JButton stu_logout;
    private javax.swing.JButton stu_pass_change;
    // End of variables declaration//GEN-END:variables
}
