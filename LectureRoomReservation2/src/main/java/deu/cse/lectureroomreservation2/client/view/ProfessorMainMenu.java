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
public class ProfessorMainMenu extends javax.swing.JFrame {

    /**
     * Creates new form ProfessorMainMenu
     */
    private final Client client;
    private final String userId;

    public ProfessorMainMenu(String userId, Client client) {
        this.client = client;
        this.userId = userId;

        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 중요!
        setSize(330, 465); // 최소한 크기 설정
        setLocationRelativeTo(null); // 가운데 정렬

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

        jLabel1 = new javax.swing.JLabel();
        resev_check = new javax.swing.JButton();
        prof_LogOut = new javax.swing.JButton();
        reserv_time_check = new javax.swing.JButton();
        quick_booking = new javax.swing.JButton();
        prof_pass_change = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("맑은 고딕", 1, 24)); // NOI18N
        jLabel1.setText("Prof Main");
        jLabel1.setToolTipText("");

        resev_check.setFont(new java.awt.Font("맑은 고딕", 1, 18)); // NOI18N
        resev_check.setText("예약 확인");
        resev_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resev_checkActionPerformed(evt);
            }
        });

        prof_LogOut.setText("로그아웃");
        prof_LogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prof_LogOutActionPerformed(evt);
            }
        });

        reserv_time_check.setFont(new java.awt.Font("맑은 고딕", 1, 18)); // NOI18N
        reserv_time_check.setText("예약 가능 시간 확인");
        reserv_time_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reserv_time_checkActionPerformed(evt);
            }
        });

        quick_booking.setFont(new java.awt.Font("맑은 고딕", 1, 18)); // NOI18N
        quick_booking.setText("빠른 예약");
        quick_booking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quick_bookingActionPerformed(evt);
            }
        });

        prof_pass_change.setText("비밀번호 변경");
        prof_pass_change.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prof_pass_changeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(prof_pass_change)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(prof_LogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(resev_check, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quick_booking, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reserv_time_check, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(reserv_time_check, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(quick_booking, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(resev_check, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(prof_pass_change, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prof_LogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void prof_LogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prof_LogOutActionPerformed
        // TODO add your handling code here:
        int choice = javax.swing.JOptionPane.showConfirmDialog(this, "로그아웃 하시겠습니까?", "로그아웃", javax.swing.JOptionPane.YES_NO_OPTION);

        if (choice == javax.swing.JOptionPane.YES_OPTION) {
            if (client != null) {
                client.logout();
            }
            this.dispose();  // 현재 창 닫기
            new LoginFrame().setVisible(true); // 로그인 화면 띄우기
        }
    }//GEN-LAST:event_prof_LogOutActionPerformed

    private void prof_pass_changeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prof_pass_changeActionPerformed
        // TODO add your handling code here:
        ChangePassView frame = new ChangePassView();
        frame.setStreams(client.getOutputStream(), client.getInputStream());
        frame.setUserId(userId);
        frame.setSize(300, 450);           // 창 크기 설정 (적당히 보기 좋은 크기)
        frame.setLocationRelativeTo(null); // 화면 가운데 정렬
        frame.setVisible(true);            // 화면에 보이게 만들기
    }//GEN-LAST:event_prof_pass_changeActionPerformed

    private void reserv_time_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reserv_time_checkActionPerformed
        // TODO add your handling code here:
        ViewRoom vroom = new ViewRoom(client, userId, "P", null);
        vroom.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_reserv_time_checkActionPerformed

    private void quick_bookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quick_bookingActionPerformed
        // TODO add your handling code here:
        QuickViewRoom qvroom = new QuickViewRoom(client, userId, "P");
        qvroom.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_quick_bookingActionPerformed

    private void resev_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resev_checkActionPerformed
        // TODO add your handling code here:
        MyReservationView myreserveview = new MyReservationView(client, userId, "P");
        myreserveview.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_resev_checkActionPerformed

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
            java.util.logging.Logger.getLogger(ProfessorMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProfessorMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProfessorMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProfessorMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProfessorMainMenu("PROFESSOR", null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton prof_LogOut;
    private javax.swing.JButton prof_pass_change;
    private javax.swing.JButton quick_booking;
    private javax.swing.JButton reserv_time_check;
    private javax.swing.JButton resev_check;
    // End of variables declaration//GEN-END:variables
}
