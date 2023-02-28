package lt.techin.springyne.controller;

import lt.techin.springyne.room.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerNoDBTest {
    @MockBean
    RoomRepository roomRepository;

    @Autowired
    RoomService roomService;

    @Test
    void addRoomReturnsSavedRoom() {
        RoomDto testRoomDto = new RoomDto(LocalDateTime.now().toString(), "Kalvarijų g. 159, LT-08313 Vilnius", "Techin");
        RoomDto testRoomDto1 = new RoomDto(null, null, null);
        RoomDto testRoomDto2 = new RoomDto("100", "Kalvarijų g. 159, LT-08313 Vilnius", "Techin");
        Room testRoom = RoomMapper.toRoom(testRoomDto);
        Room testRoom1 = RoomMapper.toRoom(testRoomDto1);
        Room testRoom2 = RoomMapper.toRoom(testRoomDto2);
        Mockito.when(roomRepository.save(testRoom)).thenReturn(testRoom);
        assertEquals(testRoom, roomService.addRoom(testRoom), "Should be able to add new Room with unique number");
        assertNull(roomService.addRoom(testRoom1),"Room with null or empty values should not be saved");
        assertNull(roomService.addRoom(testRoom2),"Room with duplicate number should not be saved");
    }
}
