/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation.control;

import deu.cse.lectureroomreservation.model.DaysOfWeek;
import deu.cse.lectureroomreservation.model.ReservationAgent;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author skylo
 */
@Slf4j
public class ReservationController {

    private ReservationAgent reservationAgent;

    public ReservationController(ReservationAgent reservationAgent) throws Exception {
        this.reservationAgent = reservationAgent;
    }

    /**
     * 강의실 사용 가능 여부를 확인한다.
     *
     * @param roomId 강의실 ID
     * @param day
     * @param fromPeriod 시작 교시
     * @param toPeriod 종료 교시
     * @return
     */
    public boolean canReserveRoom(String roomId, DaysOfWeek day, int fromPeriod, int toPeriod) {
        return this.reservationAgent.canReserveRoom(roomId, day, fromPeriod, toPeriod);
    }

    public boolean makeReservation(String roomId, DaysOfWeek day, int fromPeriod, int toPeriod, String userName) {
        return this.reservationAgent.makeReservation(roomId, day, fromPeriod, toPeriod, userName);
    }

}
