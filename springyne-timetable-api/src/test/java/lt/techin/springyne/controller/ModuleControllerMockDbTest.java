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
        Module testModule = ModuleMapper.toModule(testModuleDto);
        Mockito.when(moduleRepository.save(testModule)).thenReturn(testModule);
        assertEquals(testModule, moduleService.addModule(testModule), "Should be able to add new Module with unique number");
    }

}
