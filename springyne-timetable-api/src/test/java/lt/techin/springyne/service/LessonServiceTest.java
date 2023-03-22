package lt.techin.springyne.service;

import lt.techin.springyne.lesson.Lesson;
import lt.techin.springyne.lesson.LessonRepository;
import lt.techin.springyne.lesson.LessonService;
import lt.techin.springyne.room.RoomRepository;
import lt.techin.springyne.schedule.ScheduleRepository;
import lt.techin.springyne.subject.SubjectRepository;
import lt.techin.springyne.teacher.Teacher;
import lt.techin.springyne.teacher.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class LessonServiceTest {

    @Autowired
    private LessonService lessonService;

    @MockBean
    private LessonRepository lessonRepository;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private SubjectRepository subjectRepository;

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private RoomRepository roomRepository;

    @Test
    public void testListTeacherLessons() {
        // Given
        MockitoAnnotations.openMocks(this);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        lesson1.setTeacher(teacher);
        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);
        lesson2.setTeacher(teacher);
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson1);
        lessons.add(lesson2);

        LocalDate startDate = LocalDate.parse("2022-01-01");
        LocalDate endDate = LocalDate.parse("2022-01-31");

        when(lessonRepository.findAllByTeacherIdAndLessonDateBetween(teacher.getId(), startDate, endDate))
                .thenReturn(lessons);

        // When
        List<Lesson> result = lessonService.listTeacherLessons(teacher.getId(), startDate.toString(), endDate.toString());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(0).getTeacher().getId()).isEqualTo(1L);
        assertThat(result.get(1).getTeacher().getId()).isEqualTo(1L);
    }
}
