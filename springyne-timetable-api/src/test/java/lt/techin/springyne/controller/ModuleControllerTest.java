package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.dto.ModuleDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ModuleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllModulesContainsCorrectDtos() throws Exception {

        ModuleDto testModuleDto1 = new ModuleDto("T1", "Test name1");
        ModuleDto testModuleDto2 = new ModuleDto("T2", "Test name2");
        ModuleDto testModuleDto3 = new ModuleDto("T3", "Test name3");

        List<ModuleDto> expectedList = new ArrayList<>();
        expectedList.add(testModuleDto1);
        expectedList.add(testModuleDto2);
        expectedList.add(testModuleDto3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/modules")
                ).andExpect(status().isOk()).andReturn();

        List<ModuleDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<ModuleDto>>() {
        });

        Assertions.assertTrue(resultList.containsAll(expectedList));
    }

    @Test
    void addModuleThrowsExceptionWithNullOrEmptyValues() throws Exception {
        ModuleDto testModuleDto4 = new ModuleDto("", "Test name4");
        ModuleDto testModuleDto5 = new ModuleDto(null, "Test name5");
        ModuleDto testModuleDto6 = new ModuleDto("T6", "");
        ModuleDto testModuleDto7 = new ModuleDto("T7", null);

        String message = "Null or empty values should return bad request status";

        assertEquals(400,performModulePostBadRequest(testModuleDto4).getResponse().getStatus(), message);
        assertEquals(400,performModulePostBadRequest(testModuleDto5).getResponse().getStatus(), message);
        assertEquals(400,performModulePostBadRequest(testModuleDto6).getResponse().getStatus(), message);
        assertEquals(400,performModulePostBadRequest(testModuleDto7).getResponse().getStatus(), message);
    }

    @Test
    void addModuleThrowsExceptionWithNonUniqueNumberValue() throws Exception {
        ModuleDto testModuleDto1 = new ModuleDto("T1", "Test");
        assertEquals(400,performModulePostBadRequest(testModuleDto1).getResponse().getStatus(),
                "Non unique Module number should return bad request status");
    }


    public MvcResult performModulePostBadRequest(ModuleDto moduleDto) throws Exception {

        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/modules").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(moduleDto)))
                .andExpect(status().isBadRequest()).andReturn();
    }

}