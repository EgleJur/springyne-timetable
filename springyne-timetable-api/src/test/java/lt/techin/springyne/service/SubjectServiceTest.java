package lt.techin.springyne.service;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.repository.SubjectRepository;
import lt.techin.springyne.stubs.ModuleCreator;
import lt.techin.springyne.stubs.RoomCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static lt.techin.springyne.stubs.SubjectCreator.createSubject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    SubjectRepository subjectRepository;

    @InjectMocks
    SubjectService subjectService;

    ModuleCreator module;

    Set<RoomCreator> room = new HashSet<>();

    @Test
    void edit_subjectNotFound_throwsException() {
        var subject = createSubject(1l);

        when(subjectRepository.findById(1l)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subjectService.edit(1l, subject))
                .isInstanceOf(ScheduleValidationException.class)
                .hasMessageContaining("Subject does not exist")
                .hasFieldOrPropertyWithValue("rejectedValue", "1");
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    void create() {
        Subject subject = Subject.builder()

                .name("Ramesh")
                .description("Fadatare")
                .lastUpdated(null)
                .deleted(false)
                .module(null)
                .rooms(null)
                .build();

        subjectRepository.save(subject);

        assertThat(subject.getId()).isGreaterThan(0);
    }

    @Test
    @Order(3)
    void getAll() {



        List<Subject> subject = subjectRepository.findAll();

        assertThat(subject.size()).isGreaterThan(0);
    }


    @Test
    @Order(2)
    void getById() {
        Long id = 5l;
        var subject = new Subject();

        subject.setId(id);
        subject.setName("Math");
        subject.setDescription("Test description");
        subject.setLastUpdated(null);
        subject.setDeleted(false);
        subject.setModule(null);
        subject.setRooms(null);

        subjectRepository.save(subject);

        Subject subjectTest = subjectRepository.findById(id).orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                "id", "Subject not found", id.toString()));

        Assertions.assertThat(subjectTest.getId()).isEqualTo(id);
    }



    @Test
//    @Order(4)
//    @Rollback(value = false)
    void edit() {
        Long id = 5l;



        var subject = new Subject();

        subject.setId(id);
        subject.setName("Math");
        subject.setDescription("Test description");
        subject.setLastUpdated(null);
        subject.setDeleted(false);
        subject.setModule(null);
        subject.setRooms(null);

        subjectRepository.save(subject);

        Subject subjectFind = subjectRepository.findById(id).orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                "id", "Subject not found", id.toString()));

        subjectFind.setName(subject.getName());
        subjectFind.setDescription("S5");

        Subject subjectUpdated =  subjectRepository.save(subjectFind);

        Assertions.assertThat(subjectUpdated.getDescription()).isEqualTo("S5");

    }

    @Test
    @Order(5)
    @Rollback(value = false)
    void delete() {
        Subject subject = subjectRepository.findById(1L).get();


        subject.setDeleted(true);

        Subject subjectUpdated =  subjectRepository.save(subject);

        Assertions.assertThat(subjectUpdated.getDescription()).isEqualTo(true);

    }


}