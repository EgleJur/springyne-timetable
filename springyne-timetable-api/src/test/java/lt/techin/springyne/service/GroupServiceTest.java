package lt.techin.springyne.service;

import lt.techin.springyne.group.GroupRepository;
import lt.techin.springyne.group.GroupService;
import lt.techin.springyne.program.Program;
import lt.techin.springyne.shift.Shift;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupServiceTest {
    @InjectMocks
    GroupService groupService;
    @Mock
    GroupRepository groupRepository;

    private static final long Id = 1;
    @Mock
    Program program;
    @Mock
    Shift shift;
    
    @Test
    public void viewGroupByIdTest(){
        groupService.getById(Id);
        verify(groupRepository).findById(Id);
    }

//    @Test
//    public void editGroup() {
//        Group group = mock(Group.class);
//        when(group.getId()).thenReturn(1L);
//        when(group.getName()).thenReturn("E-22/1");
//        when(group.getGroupYear()).thenReturn("2022-2023 m.m.");
//        when(group.getStudents()).thenReturn(15);
//        when(group.getModifiedDate()).thenReturn(LocalDateTime.now());
//        when(group.isDeleted()).thenReturn(false);
//        when(group.getProgram()).thenReturn(program);
//        when(group.getShift()).thenReturn(shift);
//
//        when(groupRepository.findById(Id)).thenReturn(Optional.of(group));
//        groupService.edit(1L, group, shift.getId(), program.getId());
//        verify(groupRepository).save(group);
//    }
}


