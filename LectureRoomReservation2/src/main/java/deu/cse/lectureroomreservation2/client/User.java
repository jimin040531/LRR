/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.client;

/**
 *
 * @author namw2
 */
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;
import java.awt.Color;
import java.awt.Component;

public abstract class User {

    protected String Name;          //이름
    protected String PhoneNumber;   //휴대폰 번호 -> 비번에 사용
    protected int ReservationTime;  //예약 시간
    protected String Id;            //아이디 = 학번/교수번호/관리자는 마음대로
    protected String PassWord;      //초기 비번 = 휴대폰 번호 뒷8자리

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public int getReservationTime() {
        return ReservationTime;
    }

    public void setReservationTime(int ReservationTime) {
        this.ReservationTime = ReservationTime;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String PassWord) {
        this.PassWord = PassWord;
    }

    public abstract void ViewRoom(); //일단 필요없을거 같은데 놔두기

    //TODO 나중에 server로 옮기기
    public String[][] GetTime(String what) { //텍스트 파일을 읽어와서 배열에 저장 
        String ReservationInfoPath = "src/main/resources/ReservationInfo.txt";
        String ScheduleInfoPath = "src/main/resources/ScheduleInfo.txt";

        List<String[]> ReservationInfoList = new ArrayList<>();
        List<String[]> ScheduleInfoList = new ArrayList<>();

        try (BufferedReader Rbr = new BufferedReader(new FileReader(ReservationInfoPath)); BufferedReader Lbr = new BufferedReader(new FileReader(ScheduleInfoPath));) {
            String Rline, Lline;
            while ((Rline = Rbr.readLine()) != null) {
                // 쉼표 기준으로 나누고 배열에 저장 - 강의실 예약 정보
                String[] parts = Rline.split(",");
                ReservationInfoList.add(parts);
            }
            while ((Lline = Lbr.readLine()) != null) {
                // 쉼표 기준으로 나누고 배열에 저장 - 정규강의 정보
                String[] parts = Lline.split(",");
                ScheduleInfoList.add(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[][] Rresult = new String[ReservationInfoList.size()][];
        String[][] Sresult = new String[ScheduleInfoList.size()][];
        Rresult = ReservationInfoList.toArray(Rresult);
        Sresult = ScheduleInfoList.toArray(Sresult);

        System.out.println(Rresult[0][3]);

        for (String[] row : Rresult) {
            System.out.println(Arrays.toString(row));
        }
        for (String[] row : Sresult) {
            System.out.println(Arrays.toString(row));
        }

        if (what.equals("reservation")) {
            return Rresult;
        } else if (what.equals("schedule")) {
            return Sresult;
        }

        return Rresult;
    }

    ;
    
    public void InsertTable(String room, String day, JTable ViewTimeTable, String[][] result, String[][] result2) { //TODO if써서 reservation이랑 schedule 분할
        // DefaultTableModel을 사용하여 JTable 생성

        String[] columnNames = {"시작시간", "끝시간", "강의실번호", "예약현황"};

        // 해당 시간에 해당하는 행 찾기
        // 데이터베이스에는 행의 순서가 없다 -> 단순 for로 다 돌리면 안됨
        // for 돌리기전 선택된 호실+ 요일에 대해 배열 또 만들기
        String fixResult[][] = new String[result.length][4];
        String fixResult2[][] = new String[result2.length][4];
        int fixRow = 0;
        int fixRow2 = 0;

        for (int i = 0; i < result.length; i++) {
            if (result[i][0].equals(room) && result[i][1].equals(day)) {
                fixResult[fixRow][0] = result[i][2]; // start time
                fixResult[fixRow][1] = result[i][3]; // end time
                fixResult[fixRow][2] = result[i][0]; // room
                fixResult[fixRow][3] = result[i][4]; // state 0- 공실, 1-학생예약, 2-정규수업, 3-제한
                fixRow++;
            }
        }
        for (int i = 0; i < result2.length; i++) {
            if (result2[i][0].equals(room) && result2[i][1].equals(day)) {
                fixResult2[fixRow2][0] = result2[i][2]; // start time
                fixResult2[fixRow2][1] = result2[i][3]; // end time
                fixResult2[fixRow2][2] = result2[i][0]; // room
                if (result2[i][5].equals("수업")) {
                    fixResult2[fixRow2][4] = "2"; //state 0- 공실, 1-학생예약, 2-정규수업, 3-제한
                } else if (result2[i][5].equals("제한")) {
                    fixResult2[fixRow2][4] = "3";
                } else {
                    fixResult2[fixRow2][4] = "error"; //이거 쓸일이 없으면 좋을건데
                }
                fixRow2++;
            }
        }

        String[][] finalResult2 = new String[fixRow2][4];
        for (int i = 0; i < fixRow2; i++) {
            finalResult2[i] = fixResult2[i];
        }
        for (String[] row : finalResult2) {
            System.out.println("이건 fr2");
            System.out.println(Arrays.toString(row));
        }
        
        String[][] finalResult = new String[fixRow][4];
        for (int i = 0; i < fixRow; i++) {
            finalResult[i] = fixResult[i];
        }

        for (String[] row : finalResult) {
            System.out.println("이건 fr");
            System.out.println(Arrays.toString(row));
        }
        String[][] combinedResult = new String[finalResult.length + finalResult2.length][finalResult[0].length];
        System.arraycopy(finalResult, 0, combinedResult, 0, finalResult.length);
        System.arraycopy(finalResult2, 0, combinedResult, finalResult.length, finalResult2.length);

        
        for (String[] row : combinedResult) {
            System.out.println(Arrays.toString(row));
        }
        
        
        
        // 모델 객체 가져오기
        DefaultTableModel model = (DefaultTableModel) ViewTimeTable.getModel();

        // 학생예약이 된 행을 추적하기 위한 Set
        java.util.Set<Integer> reservedRows1 = new java.util.HashSet<>();
        java.util.Set<Integer> reservedRows2 = new java.util.HashSet<>();
        java.util.Set<Integer> reservedRows3 = new java.util.HashSet<>();

        // finalResult 형식: {startTime, endTime, room, state}
        for (String[] row : combinedResult) {
            String startTime = row[0];
            String roomNum = row[2];
            String state = row[3];

            if (state.equals("1")) { // 상태가 예약이라면
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0).equals(startTime)) {
                        model.setValueAt(roomNum, i, 2);               // Room 설정
                        model.setValueAt("학생예약", i, 3);           // State 변경
                        reservedRows1.add(i); // 색칠할 행 기억
                        break;
                    }
                }
            } else if (state.equals("2")) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0).equals(startTime)) {
                        model.setValueAt(roomNum, i, 2);               // Room 설정
                        model.setValueAt("교수예약", i, 3);           // State 변경
                        reservedRows2.add(i); // 색칠할 행 기억
                        break;
                    }
                }
            } else if (state.equals("3")) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0).equals(startTime)) {
                        model.setValueAt(roomNum, i, 2);               // Room 설정
                        model.setValueAt("사용제한", i, 3);           // State 변경
                        reservedRows3.add(i); // 색칠할 행 기억
                        break;
                    }
                }
            }
        }

        // 렌더러 정의
        TableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected && reservedRows1.contains(row)) {
                    c.setBackground(Color.PINK); // 예약된 셀 색상
                    c.setForeground(Color.BLACK);
                } else if (!isSelected && reservedRows2.contains(row)) {
                    c.setBackground(Color.YELLOW); // 예약된 셀 색상
                    c.setForeground(Color.BLACK);
                } else if (!isSelected && reservedRows3.contains(row)) {
                    c.setBackground(Color.GREEN); // 예약된 셀 색상
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };

        // 모든 열에 렌더러 적용
        for (int col = 0; col < ViewTimeTable.getColumnCount(); col++) {
            ViewTimeTable.getColumnModel().getColumn(col).setCellRenderer(colorRenderer);
        }

        ViewTimeTable.repaint(); // 테이블 다시 그리기    

    }

}
