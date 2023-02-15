package lt.techin.springyne.service;

import lt.techin.springyne.model.Subject;
import lt.techin.springyne.repository.SubjectRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class SubjectServiceTest {

    @InjectMocks
    SubjectService subjectService;
    @Mock
    SubjectRepository subjectRepository;

    private static final long Id = 1;
    @Test
    public void viewSubjectByIdTest(){
        subjectService.getById(Id);
        verify(subjectRepository).findById(Id);
    }


    @Test
    public void saveSubject(){
        Subject subject = mock(Subject.class);
        subjectService.create(subject);
        verify(subjectRepository).save(subject);
    }

    @Test
    public void editSubject() {
        Subject subject = mock(Subject.class);
        when(subject.getName()).thenReturn("R1");
        when(subject.getDescription()).thenReturn("Test");
        when(subjectRepository.findById(Id)).thenReturn(Optional.of(subject));
        subjectService.edit(Id, subject);
        verify(subjectRepository).save(subject);
    }

}