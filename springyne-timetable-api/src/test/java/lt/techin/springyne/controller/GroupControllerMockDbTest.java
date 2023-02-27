package lt.techin.springyne.controller;

import lt.techin.springyne.group.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupControllerMockDbTest {

    @MockBean
    GroupRepository groupRepository;

    @Autowired
    GroupService groupService;

    @Test
    void addGroupReturnsSavedGroup() {
        GroupDto testGroupDto = new GroupDto("E-22/1", "2023-05-05", 0);
        Group testGroup = GroupMapper.toGroup(testGroupDto);
        Mockito.when(groupRepository.save(testGroup)).thenReturn(testGroup);
        assertEquals(testGroup, groupService.addGroup(1L,1L,testGroup),
                "Should be able to create new Group with unique number");

    }

}