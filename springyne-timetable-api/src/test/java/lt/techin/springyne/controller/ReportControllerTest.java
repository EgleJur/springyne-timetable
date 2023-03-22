package lt.techin.springyne.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void exportScheduleToExcelByIdReturnsBadRequestStatusWithInvalidId() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reports/excel/schedule/0").accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        assertEquals(400, mvcResult.getResponse().getStatus(), "Get schedule by valid Id should return bad request status");
    }

    @Test
    void exportScheduleToExcelByIdReturnsOkStatusWithValidId() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reports/excel/schedule/2")
        ).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus(), "Get schedule by valid Id should return ok status");
    }
}
