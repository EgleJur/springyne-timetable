package lt.techin.springyne.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.lesson.LessonBlock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class LessonControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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
        LessonBlock testLessonBlock = new LessonBlock(LocalDate.of(2023,9,4), LocalDate.of(2023,9,4), 1, 4);

        String message = "Adding lesson on already booked group time should return bad request status";
        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lessons/schedule/2?subjectId=2&teacherId=2&roomId=5")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testLessonBlock))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void addLessonThrowsExceptionWithAlreadyBookedTeacherValues() throws Exception {
        LessonBlock testLessonBlock = new LessonBlock(LocalDate.of(2023,9,4), LocalDate.of(2023,9,4), 1, 4);

        String message = "Adding lesson on already booked group time should return bad request status";
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

}