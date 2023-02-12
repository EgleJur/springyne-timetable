package lt.techin.springyne.controller;

import lt.techin.springyne.dto.RoomDto;
import lt.techin.springyne.dto.mapper.RoomMapper;
import lt.techin.springyne.model.Room;
import lt.techin.springyne.repository.RoomRepository;
import lt.techin.springyne.service.RoomService;
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
        RoomDto testRoomDto = new RoomDto(LocalDateTime.now().toString(), "Test name1", "Test");
        RoomDto testRoomDto1 = new RoomDto(null, null, null);
        RoomDto testRoomDto2 = new RoomDto("R2", "Test name2", "Test");
        Room testRoom = RoomMapper.toRoom(testRoomDto);
        Room testRoom1 = RoomMapper.toRoom(testRoomDto1);
        Room testRoom2 = RoomMapper.toRoom(testRoomDto2);
        Mockito.when(roomRepository.save(testRoom)).thenReturn(testRoom);
        assertEquals(testRoom, roomService.addRoom(testRoom), "Should be able to add new Room with unique number");
        assertNull(roomService.addRoom(testRoom1),"Room with null or empty values should not be saved");
        assertNull(roomService.addRoom(testRoom2),"Room with duplicate number should not be saved");
    }
}
