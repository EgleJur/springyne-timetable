package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.schedule.Schedule;
import lt.techin.springyne.schedule.ScheduleController;
import lt.techin.springyne.schedule.ScheduleDto;
import lt.techin.springyne.schedule.ScheduleService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    @Test
    void getAllSchedulesContainsCorrectDtos() throws Exception {
        ScheduleDto testScheduleDto1 = new ScheduleDto("E-22/1 Java programuotojas (-a) Rytinė", LocalDate.of(2023,9,1),LocalDate.of(2023,12,22));
        ScheduleDto testScheduleDto2 = new ScheduleDto("JP-22/1 Programinės įrangos testuotojas (-a) Rytinė", LocalDate.of(2023,9,1),LocalDate.of(2023,12,22));
        ScheduleDto testScheduleDto3 = new ScheduleDto("JP-22/2 Programinės įrangos testuotojas (-a) Popietinė",LocalDate.of(2023,9,1),LocalDate.of(2023,12,22));

        List<ScheduleDto> expectedList = new ArrayList<>();
        expectedList.add(testScheduleDto1);
        expectedList.add(testScheduleDto2);
        expectedList.add(testScheduleDto3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/schedules")
        ).andExpect(status().isOk()).andReturn();

        List<ScheduleDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ScheduleDto>>() {});

        assertTrue(resultList.containsAll(expectedList));
    }

    //            creates test data in database
//        @Test
//        void addScheduleAllowsSavingWithCorrectValues() throws Exception {
//            ScheduleDto testScheduleDto = new ScheduleDto("PT-22/1 Javascript programuotojas (-a) Vakarinė", LocalDate.now().
//                    plusDays(1),LocalDate.now().plusDays(2));
//            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/schedules?groupId=4").contentType(MediaType.APPLICATION_JSON).
//                            content(objectMapper.writeValueAsString(testScheduleDto)))
//                    .andExpect(status().isOk()).andReturn();
//            assertEquals(200, mvcResult.getResponse().getStatus(), "Correct values should allow creating a new schedule");
//        }
    @Test
    void addScheduleThrowsExceptionWithNullOrEmptyValues() throws Exception {
        ScheduleDto testScheduleDto1 = new ScheduleDto("",null,null);
        ScheduleDto testScheduleDto2 = new ScheduleDto(null, null, null);
        String message = "Null or empty values should return bad request status";

        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/schedules?groupId=5").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testScheduleDto1))).andReturn();
        MvcResult mvcResult2 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/schedules?groupId=5").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testScheduleDto2))).andReturn();


        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
        assertEquals(400,mvcResult2.getResponse().getStatus(), message);
    }
    @Test
    void addScheduleThrowsExceptionWithInvalidGroupId() throws Exception {
        ScheduleDto testScheduleDto1 = new ScheduleDto("Test Name" + LocalDateTime.now(), LocalDate.now().
                    plusDays(1),LocalDate.now().plusDays(2));
        String message = "Invalid group id should return bad request status";

        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/schedules?groupId=0").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testScheduleDto1))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    @Test
    void addScheduleThrowsExceptionWithInvalidDates() throws Exception {
        ScheduleDto testScheduleDto1 = new ScheduleDto("Test Name" + LocalDateTime.now(), LocalDate.now().
                minusMonths(2),LocalDate.now().minusMonths(1));
        ScheduleDto testScheduleDto2 = new ScheduleDto("Test Name" + LocalDateTime.now(), LocalDate.now().
                plusMonths(2),LocalDate.now().plusMonths(1));

        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/schedules?groupId=5").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testScheduleDto1))).andReturn();
        MvcResult mvcResult2 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/schedules?groupId=5").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testScheduleDto2))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), "Should not allow saving schedule with dates in the past");
        assertEquals(400,mvcResult2.getResponse().getStatus(), "Should not allow saving schedule with end date before start date");
    }

    @Test
    void addScheduleThrowsExceptionWithOverlappingHours() throws Exception {
        ScheduleDto testScheduleDto1 = new ScheduleDto("Test Name" + LocalDateTime.now(), LocalDate.of(2023,10,1),
                LocalDate.of(2023,11,01));
        String message = "Overlapping dates should return bad request status";

        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/schedules?groupId=1").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testScheduleDto1))).andReturn();

        assertEquals(400,mvcResult1.getResponse().getStatus(), message);
    }

    //deletes from database
//    @Test
//    void deleteScheduleByIdDeletesScheduleAndLessons() throws Exception {
//        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/schedules/delete/1")).andExpect(status()
//                .isOk()).andReturn();
//        String message = "Should allow deleting a schedule with a valid id";
//        assertEquals(200,mvcResult1.getResponse().getStatus(), message);
//
//        MvcResult mvcResult2 = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/lessons/schedule/1")
//        ).andExpect(status().isOk()).andReturn();
//
//        List<Lesson> resultList = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<Lesson>>() {});
//        assertTrue(resultList.isEmpty());
//    }

    @Test
    void deleteScheduleByIdThrowsExceptionWithInvalidId() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/schedules/delete/0")).andExpect(status()
                .isBadRequest()).andReturn();
        String message = "Invalid schedule id should return bad request status";
        assertEquals(400,mvcResult.getResponse().getStatus(), message);
    }


    @Test
    void getScheduleByIdReturnsCorrectDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/schedules/1")
        ).andExpect(status().isOk()).andReturn();
        ScheduleDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<ScheduleDto>() {
        });
        assertEquals(result.getName(), "E-22/1 Java programuotojas (-a) Rytinė","Get schedule by Id should return schedule with correct name");
    }

    @Test
    void getScheduleByIdReturnsEmptyWithInvalidId() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/schedules/0")
        ).andReturn();
        Optional<Schedule> result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<Optional<Schedule>>() {
        });
        assertTrue(result.isEmpty(),"Get schedule by invalid Id should return empty");
    }
}

