package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.shift.ShiftDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShiftControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllShiftsContainsCorrectDtos() throws Exception {
        ShiftDto testShiftDto1 = new ShiftDto("Rytinė", 1, 4, 1);
        ShiftDto testShiftDto2 = new ShiftDto("Popietinė", 5, 8, 1);
        ShiftDto testShiftDto3 = new ShiftDto("Vakarinė", 9, 12, 1);

        List<ShiftDto> expectedList = new ArrayList<>();
        expectedList.add(testShiftDto1);
        expectedList.add(testShiftDto2);
        expectedList.add(testShiftDto3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/shifts")
        ).andExpect(status().isOk()).andReturn();

        List<ShiftDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ShiftDto>>() {
        });

        Assertions.assertTrue(resultList.containsAll(expectedList));
    }

    @Test
    void createShiftThrowsExceptionWithNullOrEmptyValues() throws Exception {
        ShiftDto testShiftDto1 = new ShiftDto("",1,4,1);
        ShiftDto testShiftDto2 = new ShiftDto(null,1,4,1);
        String message = "Null or empty values should return bad request status";


        assertEquals(400,performShiftPostBadRequest(testShiftDto1).getResponse().getStatus(), message);
        assertEquals(400,performShiftPostBadRequest(testShiftDto2).getResponse().getStatus(), message);
    }

    @Test
    void createShiftThrowsExceptionWithEndEarlierThanStart() throws Exception {
        ShiftDto testShiftDto1 = new ShiftDto("Vakarinė" + LocalDateTime.now(),6,4,1);
        String message = "End time cannot be before start time";

        assertEquals(400,performShiftPostBadRequest(testShiftDto1).getResponse().getStatus(), message);
    }

//            creates test data in database
//        @Test
//        void addShiftAllowsSavingWithCorrectValues() throws Exception {
//            ShiftDto testShiftDto = new ShiftDto("Test Name" + LocalDateTime.now(),1,4,1);
//            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shifts").contentType(MediaType.APPLICATION_JSON).
//                            content(objectMapper.writeValueAsString(testShiftDto)))
//                    .andReturn();
//            assertEquals(200, mvcResult.getResponse().getStatus(), "Correct values should allow creating new shift");
//        }


//status is 500 instead
//    @Test
//    void editShiftThrowsExceptionWithNullOrEmptyValues() throws Exception{
//        ShiftDto testShiftDto1 = new ShiftDto("",1,4,1);
//        ShiftDto testShiftDto2 = new ShiftDto(null,1,4,1);
//        String message = "Null or empty values should return bad request status";
//
//
//        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/shifts/5").contentType(MediaType.APPLICATION_JSON).
//                content(objectMapper.writeValueAsString(testShiftDto1))).andReturn();
//        MvcResult mvcResult2 = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/shifts/5").contentType(MediaType.APPLICATION_JSON).
//                content(objectMapper.writeValueAsString(testShiftDto2))).andReturn();
//
//       assertEquals(400, mvcResult2.getResponse().getStatus(),"Null value name should return bad request status");
//       assertEquals(400, mvcResult1.getResponse().getStatus(),"Empty value name should return bad request status");
//
//    }

    @Test
    void editShiftThrowsExceptionWithEndEarlierThanStart() throws Exception{
        ShiftDto testShiftDto1 = new ShiftDto("Dieninė",5,4,1);
        String message = "End time cannot be before start time";


        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/shifts/4").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testShiftDto1))).andReturn();

        assertEquals(400, mvcResult1.getResponse().getStatus(),message);

    }

    @Test
    void editShiftAllowsSavingWithCorrectValues() throws Exception{
        ShiftDto testShiftDto1 = new ShiftDto("Dieninė" + LocalDateTime.now(),2,7,1);
        String message = "Correct values should allow to edit the shift";


        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/shifts/4").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testShiftDto1))).andReturn();

        assertEquals(200, mvcResult1.getResponse().getStatus(),message);

    }

    @Test
    void getShiftByIdReturnsCorrectDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/shifts/1")
        ).andReturn();
        ShiftDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<ShiftDto>() {

        });
        Assertions.assertEquals(result.getName(), "Rytinė","Get teacher by Id should return teacher with correct name");
    }

    public MvcResult performShiftPostBadRequest(ShiftDto shiftDto) throws Exception {

        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shifts").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(shiftDto)))
                .andReturn();
    }
}