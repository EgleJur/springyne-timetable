package lt.techin.springyne.controller;

import lt.techin.springyne.dto.GroupDto;
import lt.techin.springyne.dto.mapper.GroupMapper;
import lt.techin.springyne.model.Group;
import lt.techin.springyne.repository.GroupRepository;
import lt.techin.springyne.service.GroupService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

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
        GroupDto testGroupDto = new GroupDto("E-22/1");
        Group testGroup = GroupMapper.toGroup(testGroupDto);
        Mockito.when(groupRepository.save(testGroup)).thenReturn(testGroup);
        assertEquals(testGroup, groupService.createGroup(1L,1L,testGroup),
                "Should be able to create new Group with unique number");

    }

}