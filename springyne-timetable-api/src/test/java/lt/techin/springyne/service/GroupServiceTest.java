package lt.techin.springyne.service;

import lt.techin.springyne.dto.GroupDto;
import lt.techin.springyne.model.Group;
import lt.techin.springyne.model.Room;
import lt.techin.springyne.model.Subject;
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

    @Test
    public void editGroup() {
        Group group = mock(Group.class);
        when(group.getName()).thenReturn("E-22/1");
        when(group.getGroupYear()).thenReturn("2022-2023 m.m.");
        when(group.getStudents()).thenReturn(15);
        when(groupRepository.findById(Id)).thenReturn(Optional.of(group));
        groupService.edit(Id, group, null, null);
        verify(groupRepository).save(group);
    }
}


