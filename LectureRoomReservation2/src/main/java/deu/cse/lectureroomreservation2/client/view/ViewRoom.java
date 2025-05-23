/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package deu.cse.lectureroomreservation2.client.view;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

import javax.swing.table.*;
import deu.cse.lectureroomreservation2.client.*;
import deu.cse.lectureroomreservation2.server.model.GetReservation;
import deu.cse.lectureroomreservation2.server.control.TableRearrange;

/**
 *
 * @author namw2
 */
// TODO 그거 요일을 어떻게 수정 필요할거같음 -- 좀 큰 수정필요
public class ViewRoom extends javax.swing.JFrame {

    Client client;
    String userid;
    String role;
    private static String LastUsedButton = " "; // 새로고침 기능을 위해 마지막으로 선택한 버튼 정보를 저장

    /**
     * Creates new form viewRoom
     */
    public ViewRoom(Client client, String userid, String role) {
        this.client = client;
        this.userid = userid;
        this.role = role;

        initComponents();

        ImageIcon stairIcon = new ImageIcon(getClass().getResource("/images/stairs.jpg"));
        Image stairImg = stairIcon.getImage().getScaledInstance(60, 110, Image.SCALE_SMOOTH);
        stair1.setIcon(new ImageIcon(stairImg));
        stair2.setIcon(new ImageIcon(stairImg));
        stair1.setText("");
        stair2.setText("");

        addDateComboBoxListeners();
        //updateChoosedDate(); // 초기 선택 날짜 표시

        LocalDate now = LocalDate.now();

        for (int y = now.getYear(); y <= now.getYear() + 1; y++) {
            Year.addItem(String.valueOf(y));
        }
        for (int m = 1; m <= 12; m++) {
            Month.addItem(String.format("%02d", m));
        }
        for (int d = 1; d <= 31; d++) {
            day.addItem(String.format("%02d", d));
        }
        Year.setSelectedItem(String.valueOf(now.getYear()));
        Month.setSelectedItem(String.format("%02d", now.getMonthValue()));
        day.setSelectedItem(String.format("%02d", now.getDayOfMonth()));

        // 요일 설정 (예시 : 월)
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        DayComboBox.setSelectedItem(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()));

    }

    private void addDateComboBoxListeners() {
        Year.addActionListener(e -> updateChoosedDate());
        Month.addActionListener(e -> updateChoosedDate());
        day.addActionListener(e -> updateChoosedDate());
    }

    private void updateChoosedDate() {
        String y = (String) Year.getSelectedItem();
        String m = (String) Month.getSelectedItem();
        String d = (String) day.getSelectedItem();

        if (y != null && m != null && d != null) {
            // 날짜 형식 맞추기 (월, 일이 한자리면 0 붙이기)
            if (m.length() == 1) {
                m = "0" + m;
            }
            if (d.length() == 1) {
                d = "0" + d;
            }

            ChoosedDate.setText(y + "년 " + m + "월 " + d + "일");
        }
    }

    public void insertValue(String roomNum) { // 테이블에 값을 집어넣는다.

        // Student temp = new Student("20213066", "남성우", "01076241028"); //TODO이거도 나중에수정
        String[][] tempResult = GetReservation.GetTime("reservation");
        String[][] tempResult2 = GetReservation.GetTime("schedule");

        TableRearrange.InsertTable(roomNum, (String) DayComboBox.getSelectedItem(), ViewTimeTable, tempResult,
                tempResult2);

    }

    public void updateColumnValues(JTable table) { // 비어있는 칸은 공실로 처리
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            model.setValueAt(" 공실 ", i, 3);
        }
    }

    public void loadData() {
        // SwingWorker로 서버 통신을 비동기로 처리
        SwingWorker<List<Object[]>, Void> worker = new SwingWorker<List<Object[]>, Void>() {
            @Override
            protected List<Object[]> doInBackground() {
                List<Object[]> rowDataList = new ArrayList<>();
                String year = (String) Year.getSelectedItem();
                String month = (String) Month.getSelectedItem();
                String dayOfMonth = (String) day.getSelectedItem();
                String dayOfWeek = (String) DayComboBox.getSelectedItem();
                String roomNum = LastUsedButton.trim().isEmpty() ? "908" : LastUsedButton;

                try {
                    // 서버에서 슬롯(시작,끝시간) 리스트 받아오기
                    java.util.List<String[]> slots = client.getRoomSlots(roomNum, dayOfWeek);

                    for (String[] slot : slots) {
                        String start = slot[0];
                        String end = slot[1];

                        // 날짜 포맷: yyyy / MM / dd / HH:mm HH:mm
                        String date = year + " / " + month + " / " + dayOfMonth + " / " + start + " " + end;

                        // 서버에서 상태 받아오기
                        String state = client.getRoomState(roomNum, dayOfWeek, start, end, date);

                        rowDataList.add(new Object[]{start, end, roomNum, state, dayOfWeek});
                    }
                } catch (Exception e) {
                    // 예외는 done()에서 처리
                    rowDataList.clear();
                    rowDataList.add(new Object[]{"서버 오류", "", "", "", ""});
                }
                return rowDataList;
            }

            @Override
            protected void done() {
                try {
                    List<Object[]> rowDataList = get();
                    DefaultTableModel model = (DefaultTableModel) ViewTimeTable.getModel();
                    model.setRowCount(0);
                    for (Object[] row : rowDataList) {
                        model.addRow(row);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ViewRoom.this, "서버와 통신 중 오류가 발생했습니다.\n" + e.getMessage(), "오류",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private Timer refreshTimer;

    public void refreshPage() { // TODO 버튼 누를때마다 새로운 새로고침 함수를 불러옴 갑자기 새로고침을 한번에 함
        if (refreshTimer != null && refreshTimer.isRunning()) {
            // System.out.println("이미 새로고침이 실행 중입니다.");
            return; // 이미 실행 중이면 중복 실행 방지
        }

        this.loadData();
        refreshTimer = new Timer(30_000, e -> {
            System.out.println("30초 경과 - 새로고침 중..."); // TODO 나중에 지우기
            this.loadData(); // 새로고침 동작
        });
        refreshTimer.setRepeats(true);
        refreshTimer.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        reservationPanel = new javax.swing.JPanel();
        room908 = new javax.swing.JButton();
        room911 = new javax.swing.JButton();
        room912 = new javax.swing.JButton();
        room918 = new javax.swing.JButton();
        room916 = new javax.swing.JButton();
        room915 = new javax.swing.JButton();
        room914 = new javax.swing.JButton();
        room913 = new javax.swing.JButton();
        stair2 = new javax.swing.JLabel();
        elebator = new javax.swing.JLabel();
        stair1 = new javax.swing.JLabel();
        room917 = new javax.swing.JButton();
        room910 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        reservationButton = new javax.swing.JButton();
        goBackButton = new javax.swing.JButton();
        ViewTimePane = new javax.swing.JScrollPane();
        ViewTimeTable = new javax.swing.JTable();
        DayComboBox = new javax.swing.JComboBox<>();
        DayLabel = new javax.swing.JLabel();
        RefreshButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        ChoosedDate = new javax.swing.JLabel();
        Year = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        Month = new javax.swing.JComboBox<>();
        day = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(896, 803));

        reservationPanel.setBackground(new java.awt.Color(255, 255, 255));
        reservationPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        room908.setBackground(new java.awt.Color(204, 153, 255));
        room908.setText("<html><center><b>908</b><br>세미나실</center></html>");
        room908.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                room908ActionPerformed(evt);
            }
        });

        room911.setBackground(new java.awt.Color(255, 204, 204));
        room911.setText("<html><center><b>911</b><br>실습실</center></html>");
        room911.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                room911ActionPerformed(evt);
            }
        });

        room912.setBackground(new java.awt.Color(204, 153, 255));
        room912.setText("<html><center><b>912</b><br>강의실</center></html>");
        room912.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                room912ActionPerformed(evt);
            }
        });

        room918.setBackground(new java.awt.Color(255, 204, 204));
        room918.setText("<html><center><b>918</b><br>실습실</center></html>");
        room918.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                room918ActionPerformed(evt);
            }
        });

        room916.setBackground(new java.awt.Color(255, 204, 204));
        room916.setText("<html><center><b>916</b><br>실습실</center></html>");
        room916.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                room916ActionPerformed(evt);
            }
        });

        room915.setBackground(new java.awt.Color(255, 204, 204));
        room915.setText("<html><center><b>915</b><br>실습실</center></html>");
        room915.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                room915ActionPerformed(evt);
            }
        });

        room914.setBackground(new java.awt.Color(204, 153, 255));
        room914.setText("<html><center><b>914</b><br>강의실</center></html>");
        room914.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                room914ActionPerformed(evt);
            }
        });

        room913.setBackground(new java.awt.Color(204, 153, 255));
        room913.setText("<html><center><b>913</b><br>강의실</center></html>");
        room913.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                room913ActionPerformed(evt);
            }
        });

        stair2.setText("jLabel2");

        elebator.setText("엘베 사진");

        stair1.setText("jLabel6");

        room917.setBackground(new java.awt.Color(255, 255, 204));
        room917.setText("917");

        room910.setBackground(new java.awt.Color(153, 204, 255));
        room910.setText("910");

        javax.swing.GroupLayout reservationPanelLayout = new javax.swing.GroupLayout(reservationPanel);
        reservationPanel.setLayout(reservationPanelLayout);
        reservationPanelLayout.setHorizontalGroup(
            reservationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reservationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(reservationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(reservationPanelLayout.createSequentialGroup()
                        .addComponent(room918, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(room917, javax.swing.GroupLayout.PREFERRED_SIZE, 64, Short.MAX_VALUE))
                    .addGroup(reservationPanelLayout.createSequentialGroup()
                        .addComponent(room908, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(elebator, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(reservationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(reservationPanelLayout.createSequentialGroup()
                        .addComponent(room916, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(room915, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(room914, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(room913, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(reservationPanelLayout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(stair1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(room910, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(room911, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(room912, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(stair2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        reservationPanelLayout.setVerticalGroup(
            reservationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reservationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(reservationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(reservationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(room908, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(room911, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(room912, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(stair2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(elebator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stair1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(room910, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addGroup(reservationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(reservationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(room918, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(room916, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(room915, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(room914, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(room913, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(room917, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("맑은 고딕", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("조회&예약");

        reservationButton.setText("예약");
        reservationButton.setActionCommand("reservation");
        reservationButton.setPreferredSize(new java.awt.Dimension(100, 50));
        reservationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reservationButtonActionPerformed(evt);
            }
        });

        goBackButton.setText("뒤로가기");
        goBackButton.setActionCommand("goBack");
        goBackButton.setPreferredSize(new java.awt.Dimension(100, 50));
        goBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goBackButtonActionPerformed(evt);
            }
        });

        ViewTimeTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        ViewTimeTable.setFont(new java.awt.Font("맑은 고딕", 0, 14)); // NOI18N
        ViewTimeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"09:00", "09:50", null, " 공실 "},
                {"10:00", "10:50", null, " 공실 "},
                {"11:00", "11:50", null, " 공실 "},
                {"12:00", "12:50", null, " 공실 "},
                {"13:00", "13:50", null, " 공실 "},
                {"14:00", "14:50", null, " 공실 "},
                {"15:00", "15:50", null, " 공실 "},
                {"16:00", "16:50", null, " 공실 "},
                {"17:00", "17:50", null, " 공실 "}
            },
            new String [] {
                "Title Start", "Time End", "Room", "State","Day"
            }
        )
        {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 모든 셀 편집 비활성화
            }
        }
    );
    ViewTimeTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    ViewTimeTable.setDoubleBuffered(true);
    ViewTimeTable.setGridColor(new java.awt.Color(0, 0, 0));
    ViewTimePane.setViewportView(ViewTimeTable);

    DayComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "월", "화", "수", "목", "금" }));

    DayLabel.setText("요일:");

    RefreshButton.setText("새로고침");
    RefreshButton.setActionCommand("reservation");
    RefreshButton.setPreferredSize(new java.awt.Dimension(100, 50));
    RefreshButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            RefreshButtonActionPerformed(evt);
        }
    });

    jLabel2.setText("선택날짜:");

    ChoosedDate.setText("0000년00월00일");

    Year.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "추가예정.." }));

    jLabel4.setText("날짜 변경");

    Month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

    day.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ViewTimePane)
                        .addComponent(reservationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(RefreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(reservationButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(goBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(16, 16, 16))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(6, 6, 6)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ChoosedDate, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Month, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(day, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(DayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(reservationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(DayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(DayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ChoosedDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Month, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(day, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(ViewTimePane, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(reservationButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(goBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(RefreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(15, 15, 15))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void room916ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_room916ActionPerformed
        // TODO add your handling code here:
        LastUsedButton = "916";
        this.loadData();
        this.refreshPage();
    }// GEN-LAST:event_room916ActionPerformed

    private void room918ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_room918ActionPerformed
        // TODO add your handling code here:
        LastUsedButton = "918";
        this.loadData();
        this.refreshPage();
    }// GEN-LAST:event_room918ActionPerformed

    private void room908ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_room908ActionPerformed
        // TODO add your handling code here:

        LastUsedButton = "908";
        this.loadData();
        this.refreshPage();
    }// GEN-LAST:event_room908ActionPerformed

    private void room911ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_room911ActionPerformed
        // TODO add your handling code here:
        LastUsedButton = "911";
        this.loadData();
        this.refreshPage();
    }// GEN-LAST:event_room911ActionPerformed

    private void room912ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_room912ActionPerformed
        // TODO add your handling code here:
        LastUsedButton = "912";
        this.loadData();
        this.refreshPage();
    }// GEN-LAST:event_room912ActionPerformed

    private void room913ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_room913ActionPerformed
        // TODO add your handling code here:
        LastUsedButton = "913";
        this.loadData();
        this.refreshPage();
    }// GEN-LAST:event_room913ActionPerformed

    private void room914ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_room914ActionPerformed
        // TODO add your handling code here:
        LastUsedButton = "914";
        this.loadData();
        this.refreshPage();
    }// GEN-LAST:event_room914ActionPerformed

    private void room915ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_room915ActionPerformed
        // TODO add your handling code here:
        LastUsedButton = "915";
        this.loadData();
        this.refreshPage();
    }// GEN-LAST:event_room915ActionPerformed

    private void reservationButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_reservationButtonActionPerformed
        // TODO add your handling code here:
        LastUsedButton = " ";
        /*
         * LRCompleteCheck Lcheck = new LRCompleteCheck("20211234", "S", "915",
         * "2025 / 05 / 21 12:00 13:00", "수요일", client);
         * Lcheck.setVisible(true);
         */
        this.dispose();

    }// GEN-LAST:event_reservationButtonActionPerformed

    private void goBackButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_goBackButtonActionPerformed
        // TODO add your handling code here:
        // new tempMainpage().setVisible(true);
        LastUsedButton = " ";
        if (role.equals("S")) {
            new StudentMainMenu(userid, client).setVisible(true);
        }
        if (role.equals("P")) {
            new ProfessorMainMenu(userid, client).setVisible(true);
        }
        this.dispose();
    }// GEN-LAST:event_goBackButtonActionPerformed

    private void RefreshButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_RefreshButtonActionPerformed
        // TODO add your handling code here:
        this.refreshPage();
    }// GEN-LAST:event_RefreshButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewRoom(null, null, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ChoosedDate;
    private javax.swing.JComboBox<String> DayComboBox;
    private javax.swing.JLabel DayLabel;
    private javax.swing.JComboBox<String> Month;
    private javax.swing.JButton RefreshButton;
    private javax.swing.JScrollPane ViewTimePane;
    private javax.swing.JTable ViewTimeTable;
    private javax.swing.JComboBox<String> Year;
    private javax.swing.JComboBox<String> day;
    private javax.swing.JLabel elebator;
    private javax.swing.JButton goBackButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton reservationButton;
    private javax.swing.JPanel reservationPanel;
    private javax.swing.JButton room908;
    private javax.swing.JButton room910;
    private javax.swing.JButton room911;
    private javax.swing.JButton room912;
    private javax.swing.JButton room913;
    private javax.swing.JButton room914;
    private javax.swing.JButton room915;
    private javax.swing.JButton room916;
    private javax.swing.JButton room917;
    private javax.swing.JButton room918;
    private javax.swing.JLabel stair1;
    private javax.swing.JLabel stair2;
    // End of variables declaration//GEN-END:variables
}
