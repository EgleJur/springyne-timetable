package lt.techin.springyne.service;

import lt.techin.springyne.room.Room;
import lt.techin.springyne.room.RoomRepository;
import lt.techin.springyne.room.RoomService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomServiseTest {
    @InjectMocks
    RoomService roomService;
    @Mock
    RoomRepository roomRepository;

    private static final long Id = 1;
    @Test
    public void viewRoomByIdTest(){
        roomService.viewRoom(Id);
        verify(roomRepository).findById(Id);
    }

    @Test
    private void getAllRoomsTests(){
        roomService.getAllRooms();
        verify(roomRepository).findAll();
    }

    @Test
    public void saveRoom(){
        Room room = mock(Room.class);
        roomService.addRoom(room);
        verify(roomRepository).save(room);
    }

    @Test
    public void editRoom() {
        Room room = mock(Room.class);
        when(room.getName()).thenReturn("101");
        when(room.getBuilding()).thenReturn("Lakūnų g. 3, LT-09108 Vilnius");
        when(room.getDescription()).thenReturn("Akademija.IT");
        when(roomRepository.findById(Id)).thenReturn(Optional.of(room));
        roomService.editRoom(Id, room);
        verify(roomRepository).save(room);
    }
}
