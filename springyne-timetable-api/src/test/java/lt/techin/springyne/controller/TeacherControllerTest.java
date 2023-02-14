package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.dto.TeacherDto;
import lt.techin.springyne.model.Teacher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerTest {

@Autowired
MockMvc mockMvc;

@Autowired
ObjectMapper objectMapper;

@Test
void getAllTeachersContainsCorrectDtos() throws Exception {

        TeacherDto testTeacherDto1 = new TeacherDto("T1", "Test name1", "Test lastname1");
        TeacherDto testTeacherDto2 = new TeacherDto("T2", "Test name2", "Test lastname2");
        TeacherDto testTeacherDto3 = new TeacherDto("T3", "Test name3", "Test lastname3");

        List<TeacherDto> expectedList = new ArrayList<>();
        expectedList.add(testTeacherDto1);
        expectedList.add(testTeacherDto2);
        expectedList.add(testTeacherDto3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teachers")
        ).andExpect(status().isOk()).andReturn();

        List<TeacherDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<TeacherDto>>() {
        });

        Assertions.assertTrue(resultList.containsAll(expectedList));
        }

    @Test
    void addTeacherThrowsExceptionWithNullOrEmptyValues() throws Exception {
        TeacherDto testTeacherDto4 = new TeacherDto("", "Test name4", "Test lastname4");
        TeacherDto testTeacherDto5 = new TeacherDto(null, "Test name5", "Test lastname5");
        TeacherDto testTeacherDto6 = new TeacherDto("T6", "", "");
        TeacherDto testTeacherDto7 = new TeacherDto("T7", null, null);
        String message = "Null or empty values should return bad request status";

        assertEquals(400,performTeacherPostBadRequest(testTeacherDto4).getResponse().getStatus(), message);
        assertEquals(400,performTeacherPostBadRequest(testTeacherDto5).getResponse().getStatus(), message);
        assertEquals(400,performTeacherPostBadRequest(testTeacherDto6).getResponse().getStatus(), message);
        assertEquals(400,performTeacherPostBadRequest(testTeacherDto7).getResponse().getStatus(), message);
    }

    @Test
    void addTeacherThrowsExceptionWithNonUniqueNumberValue() throws Exception {
        TeacherDto testTeacherDto1 = new TeacherDto("T1", "Test", "Test");
        assertEquals(400,performTeacherPostBadRequest(testTeacherDto1).getResponse().getStatus(),
                "Non unique Teacher number should return bad request status");
    }

    @Test
    void deleteTeacherSetsDeletedPropertyToTrue() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/teachers/delete/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Teacher resultTeacher = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Teacher>() {});
        Assertions.assertTrue(resultTeacher.isDeleted());
    }

    @Test
    void restoreTeacherSetsDeletedPropertyToFalse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/teachers/restore/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Teacher resultTeacher = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Teacher>() {});
        Assertions.assertFalse(resultTeacher.isDeleted());
    }

    @Test
    void editTeacherThrowsExceptionWithNonUniqueNumberValue() throws Exception {
        TeacherDto testTeacherDto5 = new TeacherDto("T1", "Test", "Test");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/teachers/update/5").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testTeacherDto5))).andReturn();
        assertEquals(400, mvcResult.getResponse().getStatus(),"Non unique Teacher number should return bad request status");
    }
    @Test
    void editTeacherThrowsExceptionWithEmptyValues() throws Exception {
        TeacherDto testTeacherDto5 = new TeacherDto("", "", "");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/teachers/update/5").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testTeacherDto5))).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus(),"Empty value number and name should return bad request status");
    }
    @Test
    void editTeacherAllowsSavingWithUniqueNumber() throws Exception {
        TeacherDto testTeacherDto4 = new TeacherDto(LocalDateTime.now().toString(), "test", "test");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/teachers/update/4").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testTeacherDto4))).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus(),"Unique value number and non empty name should return ok status");
    }



    public MvcResult performTeacherPostBadRequest(TeacherDto teacherDto) throws Exception {

        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/teachers").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(teacherDto)))
                .andExpect(status().isBadRequest()).andReturn();
    }
}
