/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package deu.cse.lectureroomreservation;

import deu.cse.lectureroomreservation.control.ReservationController;
import deu.cse.lectureroomreservation.model.DaysOfWeek;
import deu.cse.lectureroomreservation.model.LectureRoom;
import deu.cse.lectureroomreservation.model.ReservationAgent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Prof.Jong Min Lee
 */
@Slf4j
public class LectureRoomTest {

    public static void main(String[] args) throws Exception {
        // test1();
        test2();
    }
    
    public static void test1() throws Exception {
        List<LectureRoom> lectureRoomList1 = initialize();
        List<LectureRoom> lectureRoomList2 = initializeUsingYaml("2025학년도", "1학기");

        log.info("강의실 정보 1 = {}", lectureRoomList1);
        log.info("강의실 정보 2 = {}\n\n", lectureRoomList2);

        // IEB916호실의 3,4교시 강의 존재 여부 확인
        for (LectureRoom r : lectureRoomList2) {
            if (r.getId().equals("IEB916")) {
                int period = 3;
                boolean result = r.occupied(DaysOfWeek.TUESDAY, period);
                log.info("916호실: 화요일 {}교시 강의 존재 여부 = {}", period, result);
                period = 4;
                result = r.occupied(DaysOfWeek.TUESDAY, period);
                log.info("916호실: 화요일 {}교시 강의 존재 여부 = {}\n\n", period, result);
            }
        }

        // IEB916호실의 화요일 1,2교시 강의실 예약
        for (LectureRoom r : lectureRoomList2) {
            if (r.getId().equals("IEB916")) {
                r.addReservation(DaysOfWeek.TUESDAY, 1, 2, "홍길동");
                break;
            }
        }
        
        // IEB916호실의 화요일 2,3교시 강의실 예약
        for (LectureRoom r : lectureRoomList2) {
            if (r.getId().equals("IEB916")) {
                r.addReservation(DaysOfWeek.TUESDAY, 2, 3, "전우치");
                break;
            }
        }
        
        // IEB916호실의 화요일 3교시 강의실 예약
        for (LectureRoom r : lectureRoomList2) {
            if (r.getId().equals("IEB916")) {
                r.addReservation(DaysOfWeek.TUESDAY, 3, 3, "전우치");
                break;
            }
        }
    }
    
    public static void test2() throws Exception {
        log.info("==> test2() 시작");
        List<LectureRoom> lectureRoomList = initializeUsingYaml("2025학년도", "1학기");
        ReservationAgent agent = new ReservationAgent(lectureRoomList);
        ReservationController rc = new ReservationController(agent);
        log.info("\n\n\n");
        boolean isAvailable = rc.canReserveRoom("IEB916", DaysOfWeek.TUESDAY, 1, 2);
        log.info("916호실: 화요일 1-2교시 사용 가능 = {}", isAvailable);
        if (isAvailable) {
            boolean successful = rc.makeReservation("IEB916", DaysOfWeek.TUESDAY, 1, 2, "홍길동");
            if (successful) {
                log.info("916호실 화요일 1-2교시 강의실 예약 성공\n\n");
            } else {
                log.info("916호실 화요일 1-2교시 강의실 예약 실패\n\n");
            }
        } 
        
        isAvailable = rc.canReserveRoom("IEB918", DaysOfWeek.TUESDAY, 1, 2);
        log.info("918호실: 1-2교시 사용 가능 = {}", isAvailable);
        if (isAvailable) {
            boolean successful = rc.makeReservation("IEB918", DaysOfWeek.TUESDAY, 1, 2, "홍길동");
            if (successful) {
                log.info("918호실 1-2교시 강의실 예약 성공\n\n");
            } else {
                log.info("918호실 1-2교시 강의실 예약 실패\n\n");
            }
        }
    }

    /**
     * 강의 정보 초기화를 수행한다.
     *
     * @return
     */
    public static List<LectureRoom> initialize() {
        List<LectureRoom> lectureRoomList = new ArrayList<>();

        List<String>[] data = new ArrayList[7];
        // 916호 화요일 강의
        ArrayList<String> lectureInfo = new ArrayList<>();
        lectureInfo.add("4,OOD,1분반");
        lectureInfo.add("5,OOD,1분반");
        data[1] = new ArrayList(lectureInfo);

        // 916호 수요일 강의
        lectureInfo.clear();
        lectureInfo.add("1,SE,1분반");
        lectureInfo.add("2,SE,1분반");
        lectureInfo.add("3,SE,2분반");
        lectureInfo.add("4,SE,2분반");
        data[2] = new ArrayList(lectureInfo);

        List<String> reservationInfo = new ArrayList<>();
        reservationInfo.add("test");
        LectureRoom room = LectureRoom.builder()
                .id("IEB916")
                .lectureInfo(data)
                .reservationInfo(reservationInfo)
                .build();
        lectureRoomList.add(room);

        return lectureRoomList;
    }

    /**
     * YAML 파일을 이용한 초기화를 수행한다.
     *
     * @param year 학년도를 의미한다. 예) 2025학년
     * @param semester 학기를 의미한다. 예) 1학기
     * @return 주어진 semester에 있는 강의 정보 리스트를 반환한다.
     */
    public static List<LectureRoom> initializeUsingYaml(String year, String semester) throws Exception {
        List<LectureRoom> lectureRoomList = new ArrayList<>();

        Yaml yaml = new Yaml();
        try (InputStream is = LectureRoomTest.class.getResourceAsStream("/room_info.yml")) {
            Map<String, Map<String, Object>> obj = yaml.load(is);

            if (obj.containsKey(year) && obj.get(year).containsKey(semester)) {
                Map<String, Map<String, List<String>>> values = (Map) obj.get(year).get(semester);
                log.debug("data = {}", values);

                for (String roomId : values.keySet()) {
                    log.info("id = {}, data = {}", roomId, values.get(roomId));
                    List<String>[] data = new ArrayList[7];
                    Map<String, List<String>> info = values.get(roomId);
                    for (String day : info.keySet()) {
                        int index = switch (day.toLowerCase().substring(0, 3)) {
                            case "mon" ->
                                DaysOfWeek.MONDAY.index();
                            case "tue" ->
                                DaysOfWeek.TUESDAY.index();
                            case "wed" ->
                                DaysOfWeek.WEDNESDAY.index();
                            case "thu" ->
                                DaysOfWeek.THURSDAY.index();
                            case "fri" ->
                                DaysOfWeek.FRIDAY.index();
                            case "sat" ->
                                DaysOfWeek.SATURDAY.index();
                            case "sun" ->
                                DaysOfWeek.SUNDAY.index();
                            default -> {
                                log.warn("이 경우({})는 발생하면 안 됩니다!", day);
                                throw new Exception("wrong index");
                            }
                        };
                        log.info("day = {}, index = {}", day, index);
                        if (data[index] == null) {
                            data[index] = new ArrayList<>();
                        }
                        for (String hourlyInfo : info.get(day)) {
                            log.info("hourlyInfo = {}", hourlyInfo);
                            data[index].add(hourlyInfo);
                        }
                    }
                    LectureRoom room = LectureRoom.builder()
                            .id(roomId)
                            .lectureInfo(data)
                            .reservationInfo(new ArrayList<>())
                            .build();
                    lectureRoomList.add(room);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(LectureRoomTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lectureRoomList;
    }
}
