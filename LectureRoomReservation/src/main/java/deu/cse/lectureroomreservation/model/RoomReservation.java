/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation.model;

import lombok.Builder;
import lombok.Getter;

/**
 *
 * @author skylo
 */
@Builder
@Getter
public class RoomReservation {
    private String roomId;
    private int fromPeriod;
    private int toPeriod;
    private String personId;  // 예약자 정보. studentId 또는 professorId
    private String personName;
}
