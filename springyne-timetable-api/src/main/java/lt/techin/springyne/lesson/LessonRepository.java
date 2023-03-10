package lt.techin.springyne.lesson;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson,Long> {

    List<Lesson> findByScheduleId(Long scheduleId);

    List<Lesson> findByLessonDateBetweenAndLessonTimeBetween(LocalDate startDate, LocalDate endDate, Integer startTime, Integer endTime);

    List<Lesson> findByTeacherAndRoom(Long teacherId, Long roomId);

    Long countBySubjectIdAndSchedule_GroupId(Long subjectId, Long groupId);
}
