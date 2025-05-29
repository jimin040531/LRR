package deu.cse.lectureroomreservation2.server.control;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;
import deu.cse.lectureroomreservation2.server.model.*;
import deu.cse.lectureroomreservation2.common.*;
import deu.cse.lectureroomreservation2.server.control.receiveController;

class receiveControllerTest {
    static final Path TEST_DATA = Paths.get("src/test/resources/UserInfo_Test.txt");
    static final Path ORIGIN_DATA = Paths.get("src/main/resources/UserInfo.txt");

    @BeforeEach
    void setUp() throws Exception {
        // 테스트용 데이터 파일이 없으면 복사
        if (!Files.exists(TEST_DATA)) {
            Files.copy(ORIGIN_DATA, TEST_DATA);
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        // 테스트 후 파일 정리(필요시)
        Files.deleteIfExists(TEST_DATA);
    }

    @Test
    void testHandleReserve_Student() throws Exception {
        // 학생 예약 테스트
        ReserveRequest req = new ReserveRequest("20230001", "S", "911", "2025 / 06 / 05", "목요일", "");
        receiveController controller = new receiveController();
        ReserveResult result = controller.handleReserve(req);
        assertTrue(result.getResult(), "학생 예약은 성공해야 함");
    }

    @Test
    void testHandleReserve_Professor() throws Exception {
        // 교수 예약 테스트
        ReserveRequest req = new ReserveRequest("12345", "P", "911", "2025 / 06 / 05", "목요일", "");
        receiveController controller = new receiveController();
        ReserveResult result = controller.handleReserve(req);
        assertTrue(result.getResult(), "교수 예약은 성공해야 함");
    }

    @Test
    void testmatchReserveInfo() throws Exception {
        // 예약 정보 일치 확인
        ReserveRequest req = new ReserveRequest("20230001", "S", "911", "2025 / 06 / 05", "목요일", "");
        receiveController controller = new receiveController();
        controller.handleReserve(req);
        
        var reserves = ReserveManager.getReserveInfoById("20230001");
        assertNotNull(reserves, "예약 정보가 존재해야 함");
        assertEquals(1, reserves.size(), "예약 개수는 1개여야 함");
    }

    @Test
    void testHandleReserve_duplicate_test() throws Exception {
        // 동일 시간에 두 번 예약 시도
        ReserveRequest req = new ReserveRequest("20230001", "S", "911", "2025 / 06 / 05", "목요일", "");
        receiveController controller = new receiveController();
        controller.handleReserve(req); // 첫 예약 성공
        ReserveResult result = controller.handleReserve(req); // 두 번째 예약 실패
        assertFalse(result.getResult(), "중복 예약은 실패해야 함");
    }

    @Test
    void testHandleReserve_cancel_and_new_reserve() throws Exception {
        ReserveRequest req = new ReserveRequest("20230001", "S", "911", "2025 / 06 / 06", "금요일", "");
        receiveController controller = new receiveController();
        controller.handleReserve(req); // 예약

        // 예약 취소
        ReserveManager.cancelReserve("20230001", "911 / 2025 / 06 / 06 / 12:00 12:50 금요일");
        // 재예약
        ReserveResult result = controller.handleReserve(req);
        assertTrue(result.getResult(), "예약 취소 후 재예약은 성공해야 함");
    }
}