package lt.techin.springyne.controller;

import lt.techin.springyne.repository.GroupRepository;
import lt.techin.springyne.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupControllerMockDbTest {

    @MockBean
    GroupRepository groupRepository;

    @Autowired
    GroupService groupService;

//    @Test
//    void addGroupReturnsSavedGroup() {
//        GroupDto testGroupDto = new GroupDto(LocalDateTime.now().toString(), "Įvadas į profesiją");
//        Group testGroup = GroupMapper.toGroup(testGroupDto);
//        Mockito.when(groupRepository.save(testGroup)).thenReturn(testGroup);
//        assertEquals(testGroup, groupService.createGroup(1L,null,testGroup),
//                "Should be able to add new Group with unique number");
//
//    }

}