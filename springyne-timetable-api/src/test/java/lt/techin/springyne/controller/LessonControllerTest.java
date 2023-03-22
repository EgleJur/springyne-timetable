package lt.techin.springyne.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.lesson.Lesson;
import lt.techin.springyne.lesson.LessonBlock;
import lt.techin.springyne.lesson.LessonController;
import lt.techin.springyne.lesson.LessonService;
import lt.techin.springyne.room.Room;
import lt.techin.springyne.schedule.Schedule;
import lt.techin.springyne.shift.Shift;
import lt.techin.springyne.subject.Subject;
import lt.techin.springyne.teacher.Teacher;
import lt.techin.springyne.teacher.TeacherController;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDate;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LessonControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    private LessonController lessonController;

    @Mock
    private LessonService lessonService;
    @Mock
    TeacherController teacherService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController).build();
    }
    @Mock
    Teacher teacher;

    @Mock
    Lesson lesson;
    @Mock
    Schedule schedule;
    @Mock
    Subject subject;
    @Mock
    Room room;
    @Mock
    Shift shift;


    @Test
    public void testGetAllLessons() throws Exception {
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(1L, LocalDate.parse("2023-09-04"), 1, schedule, subject, teacher, room));
        lessons.add(new Lesson(2L, LocalDate.parse("2023-09-04"), 2, schedule, subject, teacher, room));
        when(lessonService.getAllLessons()).thenReturn(lessons);

        List<Lesson> result = lessonController.getAllLessons();

        assertEquals(lessons.size(), result.size());
        assertEquals(lessons.get(0).getLessonDate(), result.get(0).getLessonDate());
        assertEquals(lessons.get(1).getLessonDate(), result.get(1).getLessonDate());
    }

    @Test
    public void testGetLessonById() throws Exception {
        Lesson lesson = new Lesson();
        when(lessonService.getLessonById(1L)).thenReturn(Optional.of(lesson));

        mockMvc.perform(get("/api/v1/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void getAllLessonsTest(){
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);
        when(lessonService.getAllLessons()).thenReturn(lessons);
        assertEquals(lessonController.getAllLessons().size(), lessons.size());
    }

    @Test
    public void testGetLessonsBySchedule() {
        // Setup
        Long scheduleId = 1L;
        List<Lesson> expectedLessons = Arrays.asList(
                new Lesson(1L, LocalDate.parse("2023-09-04"), 1, schedule, subject, teacher, room),
                new Lesson(2L, LocalDate.parse("2023-09-04"), 1, schedule, subject, teacher, room)
        );
        when(lessonService.getLessonsBySchedule(scheduleId)).thenReturn(expectedLessons);

        // Execution
        List<Lesson> actualLessons = lessonController.getLessonsBySchedule(scheduleId);

        // Verification
        assertEquals(expectedLessons, actualLessons);
        verify(lessonService).getLessonsBySchedule(scheduleId);
    }
//    @Test
//    void addLessonThrowsExceptionWithNullValues() throws Exception {
//        LessonBlock testLessonBlock = new LessonBlock(null, null, null, null);
//
//        String message = "Null values should return bad request status";
//        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lessons/schedule/2?subjectId=1&teacherId=1&roomId=4")
//                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testLessonBlock))).andReturn();
//
//        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
//    }

    @Test
    void addLessonThrowsExceptionWithInvalidDateValues() throws Exception {
        LessonBlock testLessonBlock = new LessonBlock(LocalDate.of(2023,8,30), LocalDate.of(2023,8,31), 1, 4);

        String message = "Date values out of schedule range should return bad request status";
        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lessons/schedule/2?subjectId=1&teacherId=1&roomId=4")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testLessonBlock))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void addLessonThrowsExceptionWithInvalidTimeValues() throws Exception {
        LessonBlock testLessonBlock = new LessonBlock(LocalDate.of(2023,12,1), LocalDate.of(2023,12,2), 15, 16);

        String message = "Invalid time values should return bad request status";
        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lessons/schedule/2?subjectId=1&teacherId=1&roomId=4")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testLessonBlock))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void addLessonThrowsExceptionWithInvalidSubjectValue() throws Exception {
        LessonBlock testLessonBlock = new LessonBlock(LocalDate.of(2023,12,1), LocalDate.of(2023,12,2), 1, 4);

        String message = "Invalid subject value should return bad request status";
        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lessons/schedule/2?subjectId=0&teacherId=1&roomId=4")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testLessonBlock))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void addLessonThrowsExceptionWithInvalidTeacherValue() throws Exception {
        LessonBlock testLessonBlock = new LessonBlock(LocalDate.of(2023,12,1), LocalDate.of(2023,12,2), 1, 4);

        String message = "Invalid teacher value should return bad request status";
        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lessons/schedule/2?subjectId=1&teacherId=0&roomId=4")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testLessonBlock))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void addLessonThrowsExceptionWithInvalidRoomValue() throws Exception {
        LessonBlock testLessonBlock = new LessonBlock(LocalDate.of(2023,12,1), LocalDate.of(2023,12,2), 1, 4);

        String message = "Invalid room value should return bad request status";
        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lessons/schedule/2?subjectId=1&teacherId=1&roomId=0")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testLessonBlock))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void addLessonThrowsExceptionWithAlreadyBookedGroupValues() throws Exception {
        LessonBlock testLessonBlock = new LessonBlock(LocalDate.of(2023,9,4), LocalDate.of(2023,9,4), 9, 12);

        String message = "Adding lesson on already booked group time should return bad request status";
        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lessons/schedule/4?subjectId=2&teacherId=2&roomId=5")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testLessonBlock))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void addLessonThrowsExceptionWithAlreadyBookedTeacherValues() throws Exception {
        LessonBlock testLessonBlock = new LessonBlock(LocalDate.of(2023,9,4), LocalDate.of(2023,9,4), 1, 4);

        String message = "Adding lesson on already booked teacher time should return bad request status";
        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lessons/schedule/1?subjectId=1&teacherId=1&roomId=4")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testLessonBlock))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void addLessonThrowsExceptionWithTeacherNotTeachingSubject() throws Exception {
        LessonBlock testLessonBlock = new LessonBlock(LocalDate.of(2023,9,1), LocalDate.of(2023,9,1), 1, 4);

        String message = "Adding lesson with teacher not teaching subject should return bad request status";
        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lessons/schedule/1?subjectId=1&teacherId=3&roomId=4")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testLessonBlock))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void addLessonThrowsExceptionWithRoomNotFittingSubject() throws Exception {
        LessonBlock testLessonBlock = new LessonBlock(LocalDate.of(2023,9,1), LocalDate.of(2023,9,1), 1, 4);

        String message = "Adding lesson with room not fitting this subject should return bad request status";
        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lessons/schedule/1?subjectId=3&teacherId=3&roomId=1")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testLessonBlock))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void addLessonThrowsExceptionWithLessonTimeOutsideTeacherWorkingHours() throws Exception {
        LessonBlock testLessonBlock = new LessonBlock(LocalDate.of(2023,9,1), LocalDate.of(2023,9,1), 9, 12);

        String message = "Adding lesson with time outside of teacher's working hours should return bad request status";
        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lessons/schedule/4?subjectId=1&teacherId=1&roomId=4")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testLessonBlock))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void editLessonAllowsSavingWithCorrectValues() throws Exception{
        Long subjectId = 1L;
        Long teacherId = 1L;
        Long roomId = 4L;
        String message = "Correct values should allow to edit the lesson";

        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/lessons/editSingleLesson/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("subjectId", subjectId.toString())
                        .param("teacherId", teacherId.toString())
                        .param("roomId", roomId.toString()))
                .andReturn();

        assertEquals(200, mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void editLessonsAllowsSavingWithCorrectValues() throws Exception{
        Long scheduleId = 2L;
        Long subjectId = 1L;
        Long teacherId = 1L;
        Long roomId = 4L;
        String message = "Correct values should allow to edit the lesson";

        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/lessons/editMultipleLessons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("scheduleId", scheduleId.toString())
                        .param("subjectId", subjectId.toString())
                        .param("teacherId", teacherId.toString())
                        .param("roomId", roomId.toString()))
                .andReturn();

        assertEquals(200, mvcResult1.getResponse().getStatus(), message);
    }
    
        @Test
        public void testDeleteSingleLessonSuccess() {
            Long lessonId = 1L;
            when(lessonService.deleteLessonsByDateAndId(lessonId)).thenReturn(true);

            ResponseEntity<?> responseEntity = lessonController.deleteSingleLesson(lessonId);

            assertNotNull(responseEntity);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }

        @Test
        public void testDeleteSingleLessonFailure() {
            Long lessonId = 2L;
            when(lessonService.deleteLessonsByDateAndId(lessonId)).thenReturn(false);

            ResponseEntity<?> responseEntity = lessonController.deleteSingleLesson(lessonId);

            assertNotNull(responseEntity);
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }
        
//    @Test
//    public void testListTeacherLessons() throws Exception {
//        Long teacherId = 1L;
//        String from = "2023-09-01";
//        String to = "2023-09-30";
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        List<Lesson> lessons = Arrays.asList(
//                new Lesson(),
//                new Lesson()
//        );
//        Set<Subject> subjectSet = new HashSet<>();
//        subjectSet.add(new Subject());
//
//        Optional<Teacher> teacher = teacherService.getTeacherById(1L);
//
//        TeacherLessonPdfExporter exporter = new TeacherLessonPdfExporter(lessons, teacher);
//
//        given(lessonService.listTeacherLessons(teacherId, from, to)).willReturn(lessons);
//
//        mockMvc.perform(get("/api/v1/lessons/teachers/export/pdf")
//                        .param("teacherId", String.valueOf(teacherId))
//                        .param("startDate", from)
//                        .param("endDate", to))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)));
//        // then
//        verify(lessonService, times(1)).listTeacherLessons(teacherId, from, to);
//        verifyNoMoreInteractions(lessonService);
    //        mockMvc.perform(MockMvcRequestBuilders
//                .get("/api/lessons/teachers/export/pdf/")
//                .param("from", from)
//                        .param("to", to))
//                .content(objectMapper.writeValueAsString(testGroupDto))).andReturn();

//    }
}
