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
        TeacherDto testTeacherDto = new TeacherDto(LocalDateTime.now().toString(), "Test", "test");
// TeacherDto testTeacherDto1 = new TeacherDto(null, null);
// TeacherDto testTeacherDto2 = new TeacherDto("T2", "Test");
        Teacher testTeacher = TeacherMapper.toTeacher(testTeacherDto);
// Teacher testTeacher1 = TeacherMapper.toTeacher(testTeacherDto1);
// Teacher testTeacher2 = TeacherMapper.toTeacher(testTeacherDto2);
        Mockito.when(teacherRepository.save(testTeacher)).thenReturn(testTeacher);
        assertEquals(testTeacher, teacherService.addTeacher(testTeacher), "Should be able to add new Teacher with unique number");
// assertNull(teacherService.addTeacher(testTeacher1),"Teacher with null or empty values should not be saved");
// assertNull(teacherService.addTeacher(testTeacher2),"Teacher with duplicate number should not be saved");
    }

}