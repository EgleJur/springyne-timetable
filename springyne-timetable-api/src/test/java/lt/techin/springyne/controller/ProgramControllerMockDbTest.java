package lt.techin.springyne.controller;

import lt.techin.springyne.dto.ProgramDto;
import lt.techin.springyne.dto.mapper.ProgramMapper;
import lt.techin.springyne.model.Program;
import lt.techin.springyne.repository.ProgramRepository;
import lt.techin.springyne.service.ProgramService;
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
public class ProgramControllerMockDbTest {

    @MockBean
    ProgramRepository programRepository;

    @Autowired
    ProgramService programService;

    @Test
    void addTeacherReturnsSavedTeacher() {
        ProgramDto testProgramDto = new ProgramDto(LocalDateTime.now().toString(), "Test description", false);
        Program testProgram = ProgramMapper.toProgram(testProgramDto);
        Mockito.when(programRepository.save(testProgram)).thenReturn(testProgram);
        assertEquals(testProgram, programService.createProgram(testProgram,1L,40), "Should be able to add new Program with unique name");
    }

}