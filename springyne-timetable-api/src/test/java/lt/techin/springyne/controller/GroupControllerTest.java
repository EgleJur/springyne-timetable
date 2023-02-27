package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.group.Group;
import lt.techin.springyne.group.GroupController;
import lt.techin.springyne.group.GroupDto;
import lt.techin.springyne.group.GroupService;
import lt.techin.springyne.program.Program;
import lt.techin.springyne.shift.Shift;
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
import static org.mockito.Mockito.when;
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
        assertEquals(15, group.getStudents());
    }

    @Test
    public void viewGroupByIdTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/groups/3")
        ).andExpect(status().isOk()).andReturn();
        GroupDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<GroupDto>() {
        });
        Assertions.assertEquals(result.getName(), "JP-22/2","Get teacher by Id should return teacher with correct name");
    }

    @Test
    public void getAllGroupsTest(){
        List<Group> groups = new ArrayList<>();
        groups.add(group);
        when(groupService.getAllGroups()).thenReturn(groups);
        assertEquals(groupController.getAllGroups().size(), groups.size());
    }

    @Test
    void addGroupThrowsExceptionWithNullOrEmptyValues() throws Exception {
        GroupDto testGroupDto1 = new GroupDto("", "", 15);
        GroupDto testGroupDto2 = new GroupDto(null, null, 15);

        String message = "Null or empty values should return bad request status";

        assertEquals(400, performGroupPostBadRequest(testGroupDto1).getResponse().getStatus(), message);
        assertEquals(400, performGroupPostBadRequest(testGroupDto2).getResponse().getStatus(), message);
    }

    @Test
    void addGroupThrowsExceptionWithNonUniqueNameValue() throws Exception {

        GroupDto testGroupDto5 = new GroupDto("PT-22/2", "2022-2023m.m.",7);

        assertEquals(400, performGroupPostBadRequest(testGroupDto5).getResponse().getStatus(),
                "Non unique Group name should return bad request status");
    }

    public MvcResult performGroupPostBadRequest(GroupDto groupDto) throws Exception {

        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/groups").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(groupDto)))
                .andExpect(status().isBadRequest()).andReturn();
    }
    @Test
    void deleteGroupSetsDeletedPropertyToTrue() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/groups/delete/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Group resultGroup = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Group>() {});
        Assertions.assertTrue(resultGroup.isDeleted());
    }

    @Test
    void restoreGroupSetsDeletedPropertyToFalse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/groups/restore/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Group resultGroup = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Group>() {});
        Assertions.assertFalse(resultGroup.isDeleted());
    }

    @Test
    void editGroupThrowsExceptionWithNonUniqueNameValue() throws Exception {
        GroupDto testGroupDto = new GroupDto("E-22/1");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/groups/edit/2").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testGroupDto))).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus(),"Non unique Group name should return bad request status");
    }

    @Test
    void editGroupThrowsExceptionWithEmptyValues() throws Exception {
        GroupDto testGroupDto = new GroupDto("");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/groups/edit/1").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testGroupDto))).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus(),"Empty value name should return bad request status");
    }

    @Test
    void editGroupAllowsSavingWithUniqueName() throws Exception {

        GroupDto testGroupDto = new GroupDto("PT-22/3");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/groups/edit/5").contentType(MediaType.APPLICATION_JSON).

                content(objectMapper.writeValueAsString(testGroupDto))).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus(),"Unique value non empty name should return ok status");
    }

}


