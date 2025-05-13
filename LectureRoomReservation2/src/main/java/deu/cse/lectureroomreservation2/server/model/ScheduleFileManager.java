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
public class ScheduleFileManager {
    private final String filePath = System.getProperty("user.dir") + "/src/main/resources/ScheduleInfo.txt";

    public List<String[]> readAllLines() {
        List<String[]> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line.trim().split(","));
            }
        } catch (IOException e) {
            throw new RuntimeException("시간표 파일 읽기 실패", e);
        }
        return list;
    }

    public void appendLine(String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("시간표 추가 중 오류 발생", e);
        }
    }

    public void overwriteAll(List<String> newLines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : newLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("시간표 파일 덮어쓰기 실패", e);
        }
    }
}
