package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.dto.SubjectDto;
import lt.techin.springyne.model.Module;
import lt.techin.springyne.model.Room;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @InjectMocks
    SubjectController subjectController;

    @Mock
    SubjectService subjectService;

    @Mock
    Subject subject;

    private static final long Id = 1;
    @Test
    void getAllSubjectsContainsCorrectDtos() throws Exception {


        SubjectDto testSubjectDto1 = new SubjectDto("S1", "Test name1");
        SubjectDto testSubjectDto2 = new SubjectDto("S2", "Test name2");
        SubjectDto testSubjectDto3 = new SubjectDto("S3", "Test name3");

        List<SubjectDto> expectedList = new ArrayList<>();
        expectedList.add(testSubjectDto1);
        expectedList.add(testSubjectDto2);
        expectedList.add(testSubjectDto3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/subjects")
        ).andExpect(status().isOk()).andReturn();

        List<SubjectDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<SubjectDto>>() {
        });

        Assertions.assertTrue(resultList.containsAll(expectedList));
    }

    @Test
    void addSubjectThrowsExceptionWithNullOrEmptyValues() throws Exception {
        SubjectDto testSubjectDto4 = new SubjectDto("", "Serveriai, programiniai paketai");
        SubjectDto testSubjectDto5 = new SubjectDto(null, "Scrum procesas");
        SubjectDto testSubjectDto6 = new SubjectDto(null, null);


        String message = "Null or empty values should return bad request status";

        assertEquals(400, performSubjectPostBadRequest(testSubjectDto4).getResponse().getStatus(), message);
        assertEquals(400, performSubjectPostBadRequest(testSubjectDto5).getResponse().getStatus(), message);
        assertEquals(400, performSubjectPostBadRequest(testSubjectDto6).getResponse().getStatus(), message);
    }

    @Test
    void addSubjectThrowsExceptionWithNonUniqueNameValue() throws Exception {

        SubjectDto testSubjectDto1 = new SubjectDto("Tinklapiai", "HTML, CSS, Bootstrap");

        assertEquals(400, performSubjectPostBadRequest(testSubjectDto1).getResponse().getStatus(),
                "Non unique Subject name should return bad request status");
    }


    public MvcResult performSubjectPostBadRequest(SubjectDto subjectDto) throws Exception {

        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/subjects").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(subjectDto)))
                .andExpect(status().isBadRequest()).andReturn();
    }
    @Test
    void deleteSubjectSetsDeletedPropertyToTrue() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/subjects/delete/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Subject resultSubject = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Subject>() {});
        Assertions.assertTrue(resultSubject.isDeleted());
    }

    @Test
    void restoreSubjectSetsDeletedPropertyToFalse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/subjects/restore/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Subject resultSubject = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Subject>() {});
        Assertions.assertFalse(resultSubject.isDeleted());
    }

    @Test
    void editSubjectThrowsExceptionWithNonUniqueNameValue() throws Exception {
        SubjectDto testSubjectDto5 = new SubjectDto("Tinklapiai", "");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/subjects/edit/1?moduleId=4").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testSubjectDto5))).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus(),"Non unique Subject name should return bad request status");
    }
    @Test
    void editSubjectThrowsExceptionWithEmptyValues() throws Exception {
        SubjectDto testSubjectDto5 = new SubjectDto("", "HTML, CSS, Bootstrap");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/subjects/edit/4?moduleId=4").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testSubjectDto5))).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus(),"Empty value name should return bad request status");
    }


    @Test
    void editSubjectAllowsSavingWithUniqueName() throws Exception {

        SubjectDto testSubjectDto4 = new SubjectDto("Tarnybinės stotys ir operacinės sistemos2","Serveriai, programiniai paketai");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/subjects/edit/4?moduleId=4").contentType(MediaType.APPLICATION_JSON).

                content(objectMapper.writeValueAsString(testSubjectDto4))).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus(),"Unique value non empty name should return ok status");
    }

}


