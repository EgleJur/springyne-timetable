package lt.techin.springyne.controller;

import lt.techin.springyne.dto.TeacherDto;
import lt.techin.springyne.dto.mapper.TeacherMapper;
import lt.techin.springyne.model.Teacher;
import lt.techin.springyne.repository.TeacherRepository;
import lt.techin.springyne.service.TeacherService;
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
public class TeacherControllerMockDbTest {

    @MockBean
    TeacherRepository teacherRepository;

    @Autowired
    TeacherService teacherService;

    @Test
    void addTeacherReturnsSavedTeacher() {
        TeacherDto testTeacherDto = new TeacherDto(LocalDateTime.now().toString(), "test teams email", "test email", "test phone", 40, false);
        Teacher testTeacher = TeacherMapper.toTeacher(testTeacherDto);
        Mockito.when(teacherRepository.save(testTeacher)).thenReturn(testTeacher);
        assertEquals(testTeacher, teacherService.addTeacher(1L,1L,testTeacher), "Should be able to add new Teacher with unique number");

    }

}