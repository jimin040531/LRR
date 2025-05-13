/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package deu.cse.lectureroomreservation2;

/**
 *  
 *  데이터를 어떻게 보낼것인가?
 *  1.서버에서 가공후 전달 -> 장점: 보낼 데이터 크기가 줄어듦 단점: 필요한 데이터가있으면 각각 다른 방식으로 가공해야함
 *  2.일단 데이터 다 보내고 클라이언트가 가공 -> 장점: 서버는 함수 하나만 있으면 퉁칠수 있음 단점: 데이터 크기 큼d
 * 
 */
import deu.cse.lectureroomreservation2.client.*;

public class TestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Student a = new Student("20213066","남성우","01076241028");
        
        a.GetTime("reservation");
        
    }
    
}
