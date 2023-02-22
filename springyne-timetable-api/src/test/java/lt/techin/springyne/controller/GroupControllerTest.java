package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    GroupController groupController;

    @Mock
    GroupService groupService;

    @Mock
    Group group;

    private static final long Id = 1;
    @Test
    void getAllGroupsContainsCorrectDtos() throws Exception {

        GroupDto testGroupDto1 = new GroupDto("E-22/1", "2022-2023 m.m.", 15);
        GroupDto testGroupDto2 = new GroupDto("JP-22/1", "2022-2023 m.m.",15);
        GroupDto testGroupDto3 = new GroupDto("JP-22/2", "2021-2022 m.m.",15);

        List<GroupDto> expectedList = new ArrayList<>();
        expectedList.add(testGroupDto1);
        expectedList.add(testGroupDto2);
        expectedList.add(testGroupDto3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/groups")
        ).andExpect(status().isOk()).andReturn();

        List<GroupDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<GroupDto>>() {

        });

        Assertions.assertTrue(resultList.containsAll(expectedList));
    }

    @Test
    public void testGroupDto() {
        GroupDto group = new GroupDto("E-22/1", "2022-2023 m.m.", 15);

        assertEquals("E-22/1", group.getName());
        assertEquals("2022-2023 m.m.", group.getGroupYear());
        assertEquals(15, group.getStudents());

        group.setName("JP-22/1");
        group.setGroupYear("2022-2023 m.m.");
        group.setStudents(15);

        assertEquals("JP-22/1", group.getName());
        assertEquals("2022-2023 m.m.", group.getGroupYear());
        assertEquals(15,group.getStudents());
    }

    @Test
    public void viewGroupByIdTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/groups/1")
        ).andExpect(status().isOk()).andReturn();
        GroupDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<GroupDto>() {
        });
        Assertions.assertEquals(result.getName(), "E-22/1","Get teacher by Id should return teacher with correct name");
    }

//    @Test
//    public void getAllGroupsTest(){
//        List<Group> groups = new ArrayList<>();
//        groups.add(group);
//        when(GroupService.getAllGroups()).thenReturn(groups);
//        assertEquals(GroupController.getAllGroups().size(), groups.size());
//    }


    @Test
    void addGroupThrowsExceptionWithNullOrEmptyValues() throws Exception {
        GroupDto testGroupDto4 = new GroupDto("", "2022-2023m.m.", 15);
        GroupDto testGroupDto5 = new GroupDto(null, "2021-2022 m.m.", 10);
        GroupDto testGroupDto6 = new GroupDto(null, "2021-2022", 5);


        String message = "Null or empty values should return bad request status";

        assertEquals(400, performGroupPostBadRequest(testGroupDto4).getResponse().getStatus(), message);
        assertEquals(400, performGroupPostBadRequest(testGroupDto5).getResponse().getStatus(), message);
        assertEquals(400, performGroupPostBadRequest(testGroupDto6).getResponse().getStatus(), message);
    }
//
    @Test
    void addGroupThrowsExceptionWithNonUniqueNameValue() throws Exception {

        GroupDto testGroupDto1 = new GroupDto("E-22/1", "2022-2023m.m.",10);

        assertEquals(400, performGroupPostBadRequest(testGroupDto1).getResponse().getStatus(),
                "Non unique Group name should return bad request status");
    }
//
//        assertEquals(400, performGroupPostBadRequest(testGroupDto1).getResponse().getStatus(),
//                "Non unique Group name should return bad request status");
//    }
//
    public MvcResult performGroupPostBadRequest(GroupDto groupDto) throws Exception {

        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/groups").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(groupDto)))
                .andExpect(status().isBadRequest()).andReturn();
    }
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


