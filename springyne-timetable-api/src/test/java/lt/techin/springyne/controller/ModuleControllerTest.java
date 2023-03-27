package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.module.ModuleDto;
import lt.techin.springyne.module.Module;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
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

        ModuleDto testModuleDto1 = new ModuleDto("001", "Informacinių sistemų projektavimas");
        ModuleDto testModuleDto2 = new ModuleDto("002", "Duomenų bazių projektavimas");
        ModuleDto testModuleDto3 = new ModuleDto("003", "Programavimo aplinkos valdymas");

        List<ModuleDto> expectedList = new ArrayList<>();
        expectedList.add(testModuleDto1);
        expectedList.add(testModuleDto2);
        expectedList.add(testModuleDto3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/modules")
                ).andExpect(status().isOk()).andReturn();

        List<ModuleDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ModuleDto>>() {
        });

        Assertions.assertTrue(resultList.containsAll(expectedList));
    }

    @Test
    void addModuleThrowsExceptionWithNullOrEmptyValues() throws Exception {
        ModuleDto testModuleDto4 = new ModuleDto("", "Testavimo metodikų taikymas");
        ModuleDto testModuleDto5 = new ModuleDto(null, "Java programų kūrimas");
        ModuleDto testModuleDto6 = new ModuleDto("006", "");
        ModuleDto testModuleDto7 = new ModuleDto("007", null);

        String message = "Null or empty values should return bad request status";

        assertEquals(400,performModulePostBadRequest(testModuleDto4).getResponse().getStatus(), message);
        assertEquals(400,performModulePostBadRequest(testModuleDto5).getResponse().getStatus(), message);
        assertEquals(400,performModulePostBadRequest(testModuleDto6).getResponse().getStatus(), message);
        assertEquals(400,performModulePostBadRequest(testModuleDto7).getResponse().getStatus(), message);
    }

    @Test
    void addModuleThrowsExceptionWithNonUniqueNumberValue() throws Exception {
        ModuleDto testModuleDto1 = new ModuleDto("001", "Informacinių sistemų projektavimas");
        assertEquals(400,performModulePostBadRequest(testModuleDto1).getResponse().getStatus(),
                "Non unique Module number should return bad request status");
    }

    @Test
    @Order(1)
    @Transactional
    void deleteModuleSetsDeletedPropertyToTrue() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/modules/delete/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Module resultModule = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Module>() {});
        Assertions.assertTrue(resultModule.isDeleted());
    }

    @Test
    @Order(2)
    @Transactional
    @DependsOn("deleteModuleSetsDeletedPropertyToTrue")
    void restoreModuleSetsDeletedPropertyToFalse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/modules/restore/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Module resultModule = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Module>() {});
        Assertions.assertFalse(resultModule.isDeleted());
    }

    @Test
    void editModuleThrowsExceptionWithNonUniqueNumberValue() throws Exception {
        ModuleDto testModuleDto5 = new ModuleDto("001", "Informacinių sistemų projektavimas");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/modules/update/5").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(testModuleDto5))).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus(),"Non unique Module number should return bad request status");
    }
    @Test
    void editModuleThrowsExceptionWithEmptyValues() throws Exception {
        ModuleDto testModuleDto5 = new ModuleDto("", "");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/modules/update/5").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testModuleDto5))).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus(),"Empty value number and name should return bad request status");
    }
    @Test
    void editModuleAllowsSavingWithUniqueNumber() throws Exception {
        ModuleDto testModuleDto4 = new ModuleDto("004", "Įvadinis modulis", FALSE);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/modules/update/4").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(testModuleDto4))).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus(),"Unique value number and non empty name should return ok status");
    }

    public MvcResult performModulePostBadRequest(ModuleDto moduleDto) throws Exception {

        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/modules").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(moduleDto)))
                .andExpect(status().isBadRequest()).andReturn();
    }

}