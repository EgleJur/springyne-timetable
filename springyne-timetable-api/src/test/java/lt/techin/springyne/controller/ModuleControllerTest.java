package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.dto.ModuleDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

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
    }
}