package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.module.Module;
import lt.techin.springyne.room.Room;
import lt.techin.springyne.subject.Subject;
import lt.techin.springyne.subject.SubjectController;
import lt.techin.springyne.subject.SubjectDto;
import lt.techin.springyne.subject.SubjectService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static lt.techin.springyne.subject.SubjectMapper.toSubject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        SubjectDto testSubjectDto1 = new SubjectDto("Tinklapiai", "HTML, CSS, Bootstrap");
        SubjectDto testSubjectDto2 = new SubjectDto("Java programavimas", "Java pagrindai");
        SubjectDto testSubjectDto3 = new SubjectDto("Duomenų bazės", "DBVS ir SQL kalba");

        List<SubjectDto> expectedList = new ArrayList<>();
        expectedList.add(testSubjectDto1);
        expectedList.add(testSubjectDto2);
        expectedList.add(testSubjectDto3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/subjects")
        ).andExpect(status().isOk()).andReturn();

        List<SubjectDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<SubjectDto>>() {
        });

        Assertions.assertTrue(resultList.containsAll(expectedList));
    }

    @Test
    public void testGetSubject() throws Exception {
        Subject subject = new Subject();
        when(subjectService.getById(1L)).thenReturn(Optional.of(subject));

        mockMvc.perform(get("/api/v1/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void testSearchByNamePaged() {
        // Arrange
        String name = "Math";
        String moduleName = "Algebra";
        int page = 1;
        int pageSize = 10;
        Page<Subject> expectedPage = new PageImpl<>(Arrays.asList(new Subject(1L,"Math", "Algebra", LocalDateTime.now(), false, module,rooms)));

        Mockito.when(subjectService.searchByNamePaged(name, moduleName, page, pageSize)).thenReturn(expectedPage);

        // Act
        Page<Subject> actualPage = subjectController.searchByNamePaged(name, moduleName, page, pageSize);

        // Assert
        assertEquals(expectedPage, actualPage);
    }
    @Test
    public void createSubjectTest() {
        // Create test data
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setName("Test Subject");
        subjectDto.setDescription("This is a test subject");

        Long moduleId = 1L;
        Long roomId = 2L;

        // Set up mock service response
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Test Subject");
        subject.setDescription("This is a test subject");
        when(subjectService.createSubject(moduleId, roomId, toSubject(subjectDto))).thenReturn(subject);

        // Call controller method
        ResponseEntity<SubjectDto> responseEntity = subjectController.createSubject(subjectDto, moduleId, roomId);

        // Verify response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        SubjectDto responseDto = responseEntity.getBody();
        assertNotNull(responseDto);
        //assertEquals(subject.getId(), responseDto.getId());
        assertEquals(subject.getName(), responseDto.getName());
        assertEquals(subject.getDescription(), responseDto.getDescription());
    }

    @Test
    public void testDeleteRoomFromSubject() {
        Long subjectId = 1L;
        Long roomId = 2L;

        doNothing().when(subjectService).deleteRoomFromSubject(subjectId, roomId);

        subjectController.deleteRoomFromSubject(subjectId, roomId);

        verify(subjectService, times(1)).deleteRoomFromSubject(subjectId, roomId);
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
    public void addModuleToSubject() {
        // Arrange
        Long subjectId = 1L;
        Long moduleId = 2L;
        Subject subject = new Subject();
        Module module = new Module();
        module.setId(moduleId);
        subject.setModule(module);

        Mockito.when(subjectService.addModuleToSubject(subjectId, moduleId)).thenReturn(subject);

        // Act
        ResponseEntity<Subject> response = subjectController.addModuleToSubject(subjectId, moduleId);

        // Assert
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(subject, response.getBody());
        Mockito.verify(subjectService, Mockito.times(1)).addModuleToSubject(subjectId, moduleId);
    }

    @Test
    void editSubjectThrowsExceptionWithNonUniqueNameValue() throws Exception {
        SubjectDto testSubjectDto5 = new SubjectDto("Java programavimas", "");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/subjects/edit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSubjectDto5))).andReturn();

        assertEquals(400, mvcResult.getResponse()
                .getStatus(),"Non unique Subject name should return bad request status");
    }
    @Test
    void editSubjectThrowsExceptionWithEmptyValues() throws Exception {
        SubjectDto testSubjectDto5 = new SubjectDto("", "HTML, CSS, Bootstrap");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/subjects/edit/1?moduleId=1").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testSubjectDto5))).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus(),"Empty value name should return bad request status");
    }


    @Test
    void editSubjectAllowsSavingWithUniqueName() throws Exception {

        SubjectDto testSubjectDto4 = new SubjectDto("Tarnybinės stotys ir operacinės sistemos","Serveriai, programiniai paketai");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/subjects/edit/4")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSubjectDto4))).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus(),"Unique value non empty name should return ok status");
    }

}


