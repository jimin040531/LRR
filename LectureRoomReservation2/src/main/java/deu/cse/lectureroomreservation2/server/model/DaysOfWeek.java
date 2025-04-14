/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package deu.cse.lectureroomreservation2.server.model;

/**
 *
 * @author Prof.Jong Min Lee
 */
public enum DaysOfWeek {
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4),
    SATURDAY(5),
    SUNDAY(6);

    private final int n;

    DaysOfWeek(int n) {
        this.n = n;
    }

    public int index() {
        return this.n;
    }
}
