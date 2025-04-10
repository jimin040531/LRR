/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Prof.Jong Min Lee
 */
@Slf4j
@Builder
@Getter
@ToString
public class LectureRoom {
    
    private final String id;  // ex. IEB913, IEB916
    private final List<String>[] lectureInfo;  // ex. [null, null, ["1,SE,1분반", "2,SE,2분반", ...], null, null, null, null]
    private List<String> reservationInfo;
    
    /**
     * 강의실이 해당 교시에 강의가 있는지 확인.
     * @param day DaysOfWeek 중 하나로 요일을 의미
     * @param period 강의 시간. 1은 1교시, 2는 2교시를 의미.
     * @return 
     */
    public boolean occupied(DaysOfWeek day, int period) {
        boolean result = false;
        if (this.lectureInfo[day.index()] != null) {
            for (String item : this.lectureInfo[day.index()]) {
                String[] data = item.split(",");
                // period + "" == String.valueOf(period) == Integer.toString(period)
                if (data[0].equals(period + "")) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
    
    public boolean addReservation(DaysOfWeek day, int fromPeriod, int toPeriod, String userId) {
        boolean available = true;
        
        // 강의 시간 확인
        for (int i=fromPeriod; i<=toPeriod; i++) {
            available = available && !this.occupied(day, i);
            log.info("{}교시는 빈 강의 시간 = {}", i, !this.occupied(day, i));
        }
        // 선예약 확인
        for (String item : this.reservationInfo) {
            String[] data = item.split(",");
            log.info("기존 예약 정보 = {}", item);
            int reservedDay = Integer.parseInt(data[0]);
            int reservedPeriod = Integer.parseInt(data[1]);
            for (int i=fromPeriod; i<=toPeriod; i++) {
                if (reservedDay == day.index() && reservedPeriod == i) {
                    available = false;
                    break;
                }
            }
        }
        
        if (available) {
            for (int i=fromPeriod; i<=toPeriod; i++) {
                String data = String.format("%d,%d,%s", day.index(), i, userId);
                reservationInfo.add(data);
            }
        }
        log.info("사용 가능 여부 = {}, 갱신된 예약 정보 = {}\n\n", available, this.reservationInfo.toString());
        return available;
    }
    
}
