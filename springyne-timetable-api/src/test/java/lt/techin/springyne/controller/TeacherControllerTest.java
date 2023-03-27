package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.teacher.Teacher;
import lt.techin.springyne.teacher.TeacherDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TeacherControllerTest {

@Autowired
MockMvc mockMvc;

@Autowired
ObjectMapper objectMapper;

@Test
void getAllTeachersContainsCorrectDtos() throws Exception {

        TeacherDto testTeacherDto1 = new TeacherDto("Jonas Jonaitis", "JonasJ", "jonas@gmail.com", "822 555 22222", 35, false);
        TeacherDto testTeacherDto2 = new TeacherDto("Petras Petraitis", "PetrasP", "petras@gmail.com", "822 555 11111", 35, false);
        TeacherDto testTeacherDto3 = new TeacherDto("Antanas Antanaitis", "AntanasA", "antanas@gmail.com", "822 555 33333", 35, false);

        List<TeacherDto> expectedList = new ArrayList<>();
        expectedList.add(testTeacherDto1);
        expectedList.add(testTeacherDto2);
        expectedList.add(testTeacherDto3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teachers")
        ).andExpect(status().isOk()).andReturn();

        List<TeacherDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<TeacherDto>>() {
        });

        Assertions.assertTrue(resultList.containsAll(expectedList));

        }

    @Test
    void addTeacherThrowsExceptionWithNullOrEmptyValues() throws Exception {
        TeacherDto testTeacherDto4 = new TeacherDto("","","","",null,false);
        TeacherDto testTeacherDto5 = new TeacherDto(null, null, null, null, null, false);
        String message = "Null or empty values should return bad request status";


        assertEquals(400,performTeacherPostBadRequest(testTeacherDto4).getResponse().getStatus(), message);
        assertEquals(400,performTeacherPostBadRequest(testTeacherDto5).getResponse().getStatus(), message);
    }

    @Test
    void addTeacherThrowsExceptionWithNonExistingShiftValue() throws Exception {
        TeacherDto testTeacherDto1 = new TeacherDto("Pranas Pranaitis", "PranasP", "pranas@test.com", "pranas' test phone", 40, false);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/teachers?shiftId=0").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(testTeacherDto1)))
                .andExpect(status().isBadRequest()).andReturn();
        assertEquals(400,mvcResult.getResponse().getStatus(),
                "Non existing Shift id should return bad request status");
    }

        @Test
        void addTeacherThrowsExceptionWithNonExistingSubjectValue() throws Exception {
                TeacherDto testTeacherDto1 = new TeacherDto("Pranas Pranaitis", "PranasP", "pranas@test.com", "pranas' test phone", 40, false);
                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/teachers?shiftId=1&subjectId=0").contentType(MediaType.APPLICATION_JSON).
                                content(objectMapper.writeValueAsString(testTeacherDto1)))
                        .andExpect(status().isBadRequest()).andReturn();
                assertEquals(400,mvcResult.getResponse().getStatus(),
                        "Non existing Subject id should return bad request status");
        }


    @Test
    @Order(1)
    void deleteTeacherSetsDeletedPropertyToTrue() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/teachers/delete/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Teacher resultTeacher = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Teacher>() {});
        Assertions.assertTrue(resultTeacher.isDeleted());
    }

    @Test
    @Order(2)
    @DependsOn("deleteTeacherSetsDeletedPropertyToTrue")
    void restoreTeacherSetsDeletedPropertyToFalse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/teachers/restore/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Teacher resultTeacher = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Teacher>() {});
        Assertions.assertFalse(resultTeacher.isDeleted());
    }

    @Test
    void getTeacherByIdReturnsCorrectDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teachers/1")
        ).andExpect(status().isOk()).andReturn();
        TeacherDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<TeacherDto>() {
        });
        Assertions.assertEquals(result.getName(), "Jonas Jonaitis","Get teacher by Id should return teacher with correct name");
    }

    @Test
    void editTeacherThrowsExceptionWithEmptyValues() throws Exception {
        TeacherDto testTeacherDto1 = new TeacherDto("","","","",null, false);

        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/teachers/update/5").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testTeacherDto1))).andReturn();
//
        assertEquals(400, mvcResult1.getResponse().getStatus(),"Empty value name should return bad request status");
//
    }
    @Test
    void editTeacherAllowsSavingWithCorrectValues() throws Exception {
        TeacherDto testTeacherDto2 = new TeacherDto("Alma Almaitė", "AlmaA", "almaA@gmail.com", "822 555 44444", 35, false);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/teachers/update/4?shiftId=5&subjectId=5").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testTeacherDto2))).andReturn();

        Teacher resultTeacher = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Teacher>() {});

        assertEquals(200, mvcResult.getResponse().getStatus(),"Unique non empty name should return ok status");
        assertEquals("AlmaA", resultTeacher.getTeamsEmail(), "Should allow editing teams email");
        assertEquals("almaA@gmail.com", resultTeacher.getEmail(), "Should allow editing email");
        assertEquals("822 555 44444", resultTeacher.getPhone(), "Should allow editing phone");
        assertEquals(35, resultTeacher.getHours(), "Should allow editing hours");

    }
    @Test
    void editTeacherThrowsExceptionWithInvalidShiftValue() throws Exception {
        TeacherDto testTeacherDto = new TeacherDto("Jonė Jonienė", "JoneJ", "jone@gmail.com", "822 555 55555", 35, false);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/teachers/update/5?shiftId=0").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testTeacherDto))).andReturn();
        assertEquals(400,mvcResult.getResponse().getStatus(),
                "Non existing Shift id should return bad request status");
    }
    @Test
    void editTeacherThrowsExceptionWithInvalidSubjectValue() throws Exception {
        TeacherDto testTeacherDto = new TeacherDto("Jonė Jonienė", "JoneJ", "jone@gmail.com", "822 555 55555", 35, false);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/teachers/update/5?shiftId=5&subjectId=0").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testTeacherDto))).andReturn();
        assertEquals(400,mvcResult.getResponse().getStatus(),
                "Non existing subject id should return bad request status");
    }

    public MvcResult performTeacherPostBadRequest(TeacherDto teacherDto) throws Exception {

        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/teachers?shiftId=1").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(teacherDto)))
                .andExpect(status().isBadRequest()).andReturn();
    }
}
