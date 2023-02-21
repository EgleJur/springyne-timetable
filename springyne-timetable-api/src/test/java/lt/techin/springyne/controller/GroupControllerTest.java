package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.controller.GroupController;
import lt.techin.springyne.dto.GroupDto;
import lt.techin.springyne.model.Group;
import lt.techin.springyne.model.Program;
import lt.techin.springyne.model.Shift;
import lt.techin.springyne.service.GroupService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GroupControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Mock
    Program program;

    @Mock
    Shift shift;

    @InjectMocks
    GroupController GroupController;

    @Mock
    GroupService GroupService;

    @Mock
    Group group;

    private static final long Id = 1;
    @Test
    void getAllGroupsContainsCorrectDtos() throws Exception {

        GroupDto testGroupDto1 = new GroupDto("E-22/1", "2022-2023 m.m.");
        GroupDto testGroupDto2 = new GroupDto("JP-22/1", "2022-2023 m.m.");
        GroupDto testGroupDto3 = new GroupDto("JP-22/2", "2022-2023 m.m.");

        List<GroupDto> expectedList = new ArrayList<>();
        expectedList.add(testGroupDto1);
        expectedList.add(testGroupDto2);
        expectedList.add(testGroupDto3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/groups")
        ).andExpect(status().isOk()).andReturn();

        List<GroupDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<GroupDto>>() {
        });

        Assertions.assertTrue(resultList.containsAll(expectedList));
    }

//    @Test
//    void addGroupThrowsExceptionWithNullOrEmptyValues() throws Exception {
//        GroupDto testGroupDto4 = new GroupDto("", "Serveriai, programiniai paketai");
//        GroupDto testGroupDto5 = new GroupDto(null, "Scrum procesas");
//        GroupDto testGroupDto6 = new GroupDto(null, null);
//
//
//        String message = "Null or empty values should return bad request status";
//
//        assertEquals(400, performGroupPostBadRequest(testGroupDto4).getResponse().getStatus(), message);
//        assertEquals(400, performGroupPostBadRequest(testGroupDto5).getResponse().getStatus(), message);
//        assertEquals(400, performGroupPostBadRequest(testGroupDto6).getResponse().getStatus(), message);
//    }
//
//    @Test
//    void addGroupThrowsExceptionWithNonUniqueNameValue() throws Exception {
//
//        GroupDto testGroupDto1 = new GroupDto("Tinklapiai", "HTML, CSS, Bootstrap");
//
//        assertEquals(400, performGroupPostBadRequest(testGroupDto1).getResponse().getStatus(),
//                "Non unique Group name should return bad request status");
//    }
//
//
//    public MvcResult performGroupPostBadRequest(GroupDto groupDto) throws Exception {
//
//        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/groups").contentType(MediaType.APPLICATION_JSON).
//                        content(objectMapper.writeValueAsString(groupDto)))
//                .andExpect(status().isBadRequest()).andReturn();
//    }
//    @Test
//    void deleteGroupSetsDeletedPropertyToTrue() throws Exception {
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/groups/delete/1").contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()).andReturn();
//        Group resultGroup = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Group>() {});
//        Assertions.assertTrue(resultGroup.isDeleted());
//    }
//
//    @Test
//    void restoreGroupSetsDeletedPropertyToFalse() throws Exception {
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/groups/restore/1").contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()).andReturn();
//        Group resultGroup = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Group>() {});
//        Assertions.assertFalse(resultGroup.isDeleted());
//    }
//
//    @Test
//    void editGroupThrowsExceptionWithNonUniqueNameValue() throws Exception {
//        GroupDto testGroupDto5 = new GroupDto("Tinklapiai", "");
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/groups/edit/1?moduleId=4").contentType(MediaType.APPLICATION_JSON).
//                content(objectMapper.writeValueAsString(testGroupDto5))).andReturn();
//
//        assertEquals(400, mvcResult.getResponse().getStatus(),"Non unique Group name should return bad request status");
//    }
//    @Test
//    void editGroupThrowsExceptionWithEmptyValues() throws Exception {
//        GroupDto testGroupDto5 = new GroupDto("", "HTML, CSS, Bootstrap");
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/groups/edit/4?moduleId=4").contentType(MediaType.APPLICATION_JSON).
//                content(objectMapper.writeValueAsString(testGroupDto5))).andReturn();
//
//        assertEquals(400, mvcResult.getResponse().getStatus(),"Empty value name should return bad request status");
//    }
//
//
//    @Test
//    void editGroupAllowsSavingWithUniqueName() throws Exception {
//
//        GroupDto testGroupDto4 = new GroupDto("Tarnybinės stotys ir operacinės sistemos2","Serveriai, programiniai paketai");
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/groups/edit/4?moduleId=4").contentType(MediaType.APPLICATION_JSON).
//
//                content(objectMapper.writeValueAsString(testGroupDto4))).andReturn();
//
//        assertEquals(200, mvcResult.getResponse().getStatus(),"Unique value non empty name should return ok status");
//    }

}


