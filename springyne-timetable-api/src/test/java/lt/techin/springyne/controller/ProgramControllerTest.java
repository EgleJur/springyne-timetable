package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lt.techin.springyne.dto.ProgramDto;
import lt.techin.springyne.model.Program;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ProgramControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void getAllProgramsContainsCorrectDtos() throws Exception {
        ProgramDto testProgramDto1 = new ProgramDto("Java programuotojas (-a)", "Java programuotojo modulinė profesinio mokymo programa", false);
        ProgramDto testProgramDto2 = new ProgramDto("Programinės įrangos testuotojas (-a)", "Programinės įrangos testuotojo modulinė profesinio mokymo programa", false);
        ProgramDto testProgramDto3 = new ProgramDto("Javascript programuotojas (-a)","JavaScript programuotojo modulinė profesinio mokymo programa", false);

        List<ProgramDto> expectedList = new ArrayList<>();
        expectedList.add(testProgramDto1);
        expectedList.add(testProgramDto2);
        expectedList.add(testProgramDto3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/programs")
        ).andExpect(status().isOk()).andReturn();

        List<ProgramDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ProgramDto>>() {});

        log.info(resultList.toString());

        Assertions.assertTrue(resultList.containsAll(expectedList));
    }

//            creates test data in database
//        @Test
//        void addProgramAllowsSavingWithCorrectValues() throws Exception {
//            ProgramDto testProgramDto = new ProgramDto("Test Name" + LocalDateTime.now(), "Test description", false);
//            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/programs?subjectId=1&hours=100").contentType(MediaType.APPLICATION_JSON).
//                            content(objectMapper.writeValueAsString(testProgramDto)))
//                    .andExpect(status().isOk()).andReturn();
//            assertEquals(200, mvcResult.getResponse().getStatus(), "Correct values should allow creating a new program");
//        }

    @Test
    void addProgramThrowsExceptionWithNullOrEmptyValues() throws Exception {
        ProgramDto testProgramDto1 = new ProgramDto("","",false);
        ProgramDto testProgramDto2 = new ProgramDto(null, null, false);
        String message = "Null or empty values should return bad request status";

        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/programs?subjectId=1&hours=100").contentType(MediaType.APPLICATION_JSON).
                            content(objectMapper.writeValueAsString(testProgramDto1))).andReturn();
        MvcResult mvcResult2 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/programs?subjectId=1&hours=100").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testProgramDto2))).andReturn();


        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
        assertEquals(400,mvcResult2.getResponse().getStatus(), message);
    }
    @Test
    void addProgramThrowsExceptionWithInvalidSubjectId() throws Exception {
        ProgramDto testProgramDto1 = new ProgramDto("Test Name" + LocalDateTime.now(), "Test description", false);
        String message = "Invalid subject id should return bad request status";

        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/programs?subjectId=0&hours=100").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testProgramDto1))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void addProgramThrowsExceptionWithInvalidHours() throws Exception {
        ProgramDto testProgramDto1 = new ProgramDto("Test Name" + LocalDateTime.now(), "Test description", false);
        String message = "Invalid hours should return bad request status";

        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/programs?subjectId=1&hours=-1").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testProgramDto1))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void getProgramByIdReturnsCorrectDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/programs/1")
        ).andExpect(status().isOk()).andReturn();
        ProgramDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<ProgramDto>() {
        });
        assertEquals(result.getName(), "Java programuotojas (-a)","Get program by Id should return program with correct name");
    }

    @Test
    void getProgramByIdReturnsEmptyWithInvalidId() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/programs/0")
        ).andReturn();
        Optional<Program> result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<Optional<Program>>() {
        });
        Assertions.assertTrue(result.isEmpty(),"Get program by invalid Id should return empty");
    }

}