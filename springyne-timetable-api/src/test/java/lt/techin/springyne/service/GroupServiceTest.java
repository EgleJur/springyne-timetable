package lt.techin.springyne.service;

import lt.techin.springyne.model.Group;
import lt.techin.springyne.model.Room;
import lt.techin.springyne.repository.GroupRepository;
import lt.techin.springyne.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupServiceTest {
    @InjectMocks
    GroupService groupService;
    @Mock
    GroupRepository groupRepository;

    private static final long Id = 1;
    @Test
    public void viewGroupByIdTest(){
        groupService.getById(Id);
        verify(groupRepository).findById(Id);
    }


//    @Test
//    public void saveGroup(){
//        Group group = mock(Group.class);
//        groupService.addGroup(group);
//        verify(groupRepository).save(group);
//    }

//    @Test
//    public void editGroup() {
//        Group group = mock(Group.class);
//        when(group.getName()).thenReturn("101");
//        when(room.getBuilding()).thenReturn("Lakūnų g. 3, LT-09108 Vilnius");
//        when(room.getDescription()).thenReturn("Akademija.IT");
//        when(roomRepository.findById(Id)).thenReturn(Optional.of(room));
//        roomService.editRoom(Id, room);
//        verify(roomRepository).save(room);
//    }
}


