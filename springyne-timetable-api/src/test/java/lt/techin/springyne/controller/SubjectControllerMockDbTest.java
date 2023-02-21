package lt.techin.springyne.controller;

import lt.techin.springyne.dto.SubjectDto;
import lt.techin.springyne.dto.mapper.SubjectMapper;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.repository.SubjectRepository;
import lt.techin.springyne.service.SubjectService;
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
public class SubjectControllerMockDbTest {

    @MockBean
    SubjectRepository subjectRepository;

    @Autowired
    SubjectService subjectService;

    @Test
    void addSubjectReturnsSavedSubject() {
        SubjectDto testSubjectDto = new SubjectDto(LocalDateTime.now().toString(), "Test");
        Subject testSubject = SubjectMapper.toSubject(testSubjectDto);
        Mockito.when(subjectRepository.save(testSubject)).thenReturn(testSubject);
        assertEquals(testSubject, subjectService.createSubject(1L,null,testSubject), "Should be able to add new Subject with unique number");

    }

}