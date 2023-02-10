package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.dto.SubjectDto;
import lt.techin.springyne.model.Module;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.service.SubjectService;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SubjectControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Mock
    Module module;

    @Mock
    Room room;
    Set<Module> modules = new HashSet<>();
    Set<Room> rooms = new HashSet<>();
    @Test
    void getAllSubjectsContainsCorrectDtos() throws Exception {


        SubjectDto testSubjectDto1 = new SubjectDto("R1", "Test name1", modules, rooms);
        SubjectDto testSubjectDto2 = new SubjectDto("R2", "Test name2", modules, rooms);
        SubjectDto testSubjectDto3 = new SubjectDto("R3", "Test name3", modules, rooms);

        List<SubjectDto> expectedList = new ArrayList<>();
        expectedList.add(testSubjectDto1);
        expectedList.add(testSubjectDto2);
        expectedList.add(testSubjectDto3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/subject")
        ).andExpect(status().isOk()).andReturn();

        List<SubjectDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<SubjectDto>>() {
        });

        Assertions.assertTrue(resultList.containsAll(expectedList));
    }

    @Test
    void addSubjectThrowsExceptionWithNullOrEmptyValues() throws Exception {
        SubjectDto testSubjectDto4 = new SubjectDto("", "Test name4", modules, rooms);
        SubjectDto testSubjectDto5 = new SubjectDto(null, "Test name5", modules, rooms);
        SubjectDto testSubjectDto6 = new SubjectDto(null, null, null, null);


        String message = "Null or empty values should return bad request status";

        assertEquals(400, performSubjectPostBadRequest(testSubjectDto4).getResponse().getStatus(), message);
        assertEquals(400, performSubjectPostBadRequest(testSubjectDto5).getResponse().getStatus(), message);
        assertEquals(400, performSubjectPostBadRequest(testSubjectDto6).getResponse().getStatus(), message);
    }

    @Test
    void addModuleThrowsExceptionWithNonUniqueNumberValue() throws Exception {
        SubjectDto testSubjectDto1 = new SubjectDto("R1", "Test name1", modules, rooms);
        assertEquals(400, performSubjectPostBadRequest(testSubjectDto1).getResponse().getStatus(),
                "Non unique Subject number should return bad request status");
    }


    public MvcResult performSubjectPostBadRequest(SubjectDto subjectDto) throws Exception {

        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/subject").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(subjectDto)))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @InjectMocks
    SubjectControler subjectControler;

    @Mock
    SubjectService subjectService;

    @Mock
    Subject subject;

    private static final long Id = 1;

    @Test
    public void viewSubjectByIdTest() {
        when(subjectService.getById(Id)).thenReturn(Optional.of(subject));
        assertEquals(subjectControler.getSubject(Id).getBody(), subject);
    }

    @Test
    public void getAllSubjectsTest() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(subject);
        when(subjectService.getAll()).thenReturn(subjects);
        assertEquals(subjectControler.getAllSubjects().size(), subjects.size());
    }
}
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Test
//    void getAllSubjectsContainsCorrectDtos() throws Exception {
//
//        SubjectDto testSubjectDto1 = new SubjectDto("T1", "Test name1", null, null);
//        SubjectDto testSubjectDto2 = new SubjectDto("T2", "Test name2", null, null);
//        SubjectDto testSubjectDto3 = new SubjectDto("T3", "Test name3", null, null);
//
//        List<SubjectDto> expectedList = new ArrayList<>();
//        expectedList.add(testSubjectDto1);
//        expectedList.add(testSubjectDto2);
//        expectedList.add(testSubjectDto3);
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/subject")
//        ).andExpect(status().isOk()).andReturn();
//
//        List<SubjectDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<SubjectDto>>() {
//        });
//
//        Assertions.assertTrue(resultList.containsAll(expectedList));
//    }
//
//    @Test
//    void createSubjectThrowsExceptionWithNullOrEmptyValues() throws Exception {
//        SubjectDto testSubjectDto4 = new SubjectDto("", "Test name4", null, null);
//        SubjectDto testSubjectDto5 = new SubjectDto(null, "Test name5", null, null);
//        SubjectDto testSubjectDto6 = new SubjectDto("T6", "", null, null);
//        SubjectDto testSubjectDto7 = new SubjectDto("T7", null, null, null);
//
//        String message = "Null or empty values should return bad request status";
//
//        assertEquals(400,performSubjectPostBadRequest(testSubjectDto4).getResponse().getStatus(), message);
//        assertEquals(400,performSubjectPostBadRequest(testSubjectDto5).getResponse().getStatus(), message);
//        assertEquals(400,performSubjectPostBadRequest(testSubjectDto6).getResponse().getStatus(), message);
//        assertEquals(400,performSubjectPostBadRequest(testSubjectDto7).getResponse().getStatus(), message);
//    }
//
//    @Test
//    void addSubjectThrowsExceptionWithNonUniqueNumberValue() throws Exception {
//        SubjectDto testSubjectDto1 = new SubjectDto("T1", "Test", null, null);
//        assertEquals(400,performSubjectPostBadRequest(testSubjectDto1).getResponse().getStatus(),
//                "Non unique Module number should return bad request status");
//    }
//
//
//    public MvcResult performSubjectPostBadRequest(SubjectDto subjectDto) throws Exception {
//
//        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/subject").contentType(MediaType.APPLICATION_JSON).
//                        content(objectMapper.writeValueAsString(subjectDto)))
//                .andExpect(status().isBadRequest()).andReturn();
//    }
//
//}
