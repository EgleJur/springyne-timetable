package lt.techin.springyne.controller;

import lt.techin.springyne.dto.ModuleDto;
import lt.techin.springyne.dto.mapper.ModuleMapper;
import lt.techin.springyne.model.Module;
import lt.techin.springyne.repository.ModuleRepository;
import lt.techin.springyne.service.ModuleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureMockMvc
public class ModuleControllerMockDbTest {

    @MockBean
    ModuleRepository moduleRepository;

    @Autowired
    ModuleService moduleService;

    @Test
    void addModuleReturnsSavedModule() {
        ModuleDto testModuleDto = new ModuleDto(LocalDateTime.now().toString(), "Test");
        ModuleDto testModuleDto1 = new ModuleDto(null, null);
        ModuleDto testModuleDto2 = new ModuleDto("T2", "Test");
        Module testModule = ModuleMapper.toModule(testModuleDto);
        Module testModule1 = ModuleMapper.toModule(testModuleDto1);
        Module testModule2 = ModuleMapper.toModule(testModuleDto2);
        Mockito.when(moduleRepository.save(testModule)).thenReturn(testModule);
        assertEquals(testModule, moduleService.addModule(testModule), "Should be able to add new Module with unique number");
        assertNull(moduleService.addModule(testModule1),"Module with null or empty values should not be saved");
        assertNull(moduleService.addModule(testModule2),"Module with duplicate number should not be saved");
    }

}
