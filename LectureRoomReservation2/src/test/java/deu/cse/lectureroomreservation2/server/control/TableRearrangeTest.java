/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.control;

/**
 *
 * @author namw2
 */
import deu.cse.lectureroomreservation2.client.TableRearrange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static org.junit.jupiter.api.Assertions.*;

public class TableRearrangeTest {

    private JTable viewTimeTable;
    private JTable quickViewTimeTable;

    @BeforeEach
    void setupTables() {
        // ViewTimeTable용 (InsertTable에서 사용하는 테이블 구조)
        String[] viewColumns = {"Title Start", "Time End", "Room", "State", "Day"};
        Object[][] viewData = {
            {"09:00", "09:50", null, "공실", null},
            {"10:00", "10:50", null, "공실", null},
            {"11:00", "11:50", null, "공실", null},
            {"12:00", "12:50", null, "공실", null},
            {"13:00", "13:50", null, "공실", null},
            {"14:00", "14:50", null, "공실", null},
            {"15:00", "15:50", null, "공실", null},
            {"16:00", "16:50", null, "공실", null},
            {"17:00", "17:50", null, "공실", null}
        };
        DefaultTableModel viewModel = new DefaultTableModel(viewData, viewColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        viewTimeTable = new JTable(viewModel);

        // QuickViewTimeTable용 (QuickInsertTable2에서 사용하는 테이블 구조)
        String[] quickColumns = {"Time Start", "Time End", "Room", "State", "Day"};
        Object[][] quickData = {
            {"09:00", "09:50", null, "공실", "화"},
            {"10:00", "10:50", null, "공실", "화"},
            {"11:00", "11:50", null, "공실", "화"},
            {"12:00", "12:50", null, "공실", "화"},
            {"13:00", "13:50", null, "공실", "화"},
            {"14:00", "14:50", null, "공실", "화"},
            {"15:00", "15:50", null, "공실", "화"},
            {"16:00", "16:50", null, "공실", "화"},
            {"17:00", "17:50", null, "공실", "화"}
        };
        DefaultTableModel quickModel = new DefaultTableModel(quickData, quickColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        quickViewTimeTable = new JTable(quickModel);
    }

    @Test
    void testInsertTable() {
        // 2025/05/20 = 화요일
        String[][] reservationData = {
            {"908", "월", "09:00", "09:50", "1", "20230007", "25/05/26"},
            {"907", "월", "12:00", "12:50", "1", "20230008", "25/05/26"}
        };
        String[][] scheduleData = {
            {"911", "월", "15:00", "15:50", "컴퓨터비전응용", "홍길동"},
            {"909", "월", "11:00", "11:50", "컴퓨터비전응용", "홍길동"}
        };

        TableRearrange.InsertTable("908", "월", viewTimeTable, reservationData, scheduleData);

        for (int i = 0; i < 9; i++) {
            System.out.println("확인용" + viewTimeTable.getValueAt(i, 0));
            System.out.println("확인용" + viewTimeTable.getValueAt(i, 1));
            System.out.println("확인용" + viewTimeTable.getValueAt(i, 2));
            System.out.println("확인용" + viewTimeTable.getValueAt(i, 3));
            System.out.println("확인용" + viewTimeTable.getValueAt(i, 4));
        }
        
        assertEquals("908", viewTimeTable.getValueAt(0, 2)); // 
        assertEquals("학생예약", viewTimeTable.getValueAt(0, 3)); // 
        assertEquals("25/05/26", viewTimeTable.getValueAt(0, 4)); // 
    }

    @Test
    void testQuickInsertTable2() {
        String[][] reservationData = {
            {"908", "화", "09:00", "09:50", "1", "20230007", "25/05/20"},
            {"908", "화", "10:00", "10:50", "1", "20230008", "25/05/20"}
        };
        String[][] scheduleData = {
            {"908", "화", "11:00", "11:50", "컴퓨터비전응용", "홍길동"},
            {"911", "월", "15:00", "15:50", "컴퓨터비전응용", "홍길동"}
        };

        TableRearrange.QuickInsertTable2(quickViewTimeTable, reservationData, scheduleData, "20230007");

        for (int i = 0; i < 5; i++) {
            System.out.println("확인용" + quickViewTimeTable.getValueAt(i, 0));
            System.out.println("확인용" + quickViewTimeTable.getValueAt(i, 1));
            System.out.println("확인용" + quickViewTimeTable.getValueAt(i, 2));
            System.out.println("확인용" + quickViewTimeTable.getValueAt(i, 3));
            System.out.println("확인용" + quickViewTimeTable.getValueAt(i, 4));
        }
        
        assertEquals("12:00", quickViewTimeTable.getValueAt(0, 0)); // 
        assertEquals("908", quickViewTimeTable.getValueAt(0, 2)); //
        assertEquals("공실", quickViewTimeTable.getValueAt(0, 3)); // 
        assertEquals("25/05/21", quickViewTimeTable.getValueAt(0, 4)); //여기에 오늘 날짜를 기입해야 테스트 통과가능
 
    }
}
