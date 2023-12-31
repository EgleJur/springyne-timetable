package lt.techin.springyne.lesson;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson,Long> {

    List<Lesson> findByScheduleId(Long scheduleId);

    List<Lesson> findByLessonDateBetweenAndLessonTimeBetween(LocalDate startDate, LocalDate endDate, Integer startTime, Integer endTime);

    Long countBySubjectIdAndSchedule_GroupId(Long subjectId, Long groupId);

    List<Lesson> findBySubjectIdAndScheduleIdAndTeacherIdAndRoomId(Long subjectId, Long sheduleId, Long teacherId, Long roomId);
    List<Lesson> findAllBySubjectIdAndScheduleId(Long subjectId, Long sheduleId);

    Lesson findBySubjectIdAndScheduleId(Long subjectId, Long sheduleId);

    List<Lesson> findAllByLessonDateAndSubjectId(LocalDate lessonDate, Long subjectId);
    List<Lesson> findAllByLessonDateAndSubjectIdAndScheduleId(LocalDate lessonDate, Long subjectId, Long scheduleId);
    Lesson findByLessonDateAndTeacherIdAndLessonTime(LocalDate lessonDate, Long teacherId, Integer lessonTime);

    Lesson findByLessonDateAndRoomIdAndLessonTime(LocalDate lessonDate, Long roomId, Integer lessonTime);

    List<Lesson> findAllByTeacherId(Long teacherId);
    List<Lesson> findAllByTeacherIdAndLessonDate(Long teacherId, LocalDate lessonDate);
    List<Lesson> findAllByTeacherIdAndLessonDateBetweenOrderByLessonDateAscLessonTimeAsc(Long teacherId, LocalDate startDate, LocalDate endDate);

    List<Lesson> findAllByRoomIdAndLessonDateBetweenOrderByLessonDateAscLessonTimeAsc(Long roomId, LocalDate startDate, LocalDate endDate);
    List<Lesson> findByLessonDate(LocalDate lessonDate);

    List<Lesson> findByScheduleIdOrderByLessonDateAscLessonTimeAsc(Long scheduleId);

    List<Lesson> findAllByRoomIdAndLessonDate(Long roomId, LocalDate lessonDate);

    List<Lesson> findAllByTeacherIdAndLessonDateAndScheduleIdNot(Long teacherId, LocalDate lessonDate, Long ScheduleId);
    List<Lesson> findAllByTeacherIdAndScheduleIdNot(Long teacherId, Long ScheduleId);

    List<Lesson> findAllByRoomIdAndLessonDateAndScheduleIdNot(Long roomId, LocalDate lessonDate, Long scheduleId);
    List<Lesson> findAllByRoomIdAndScheduleIdNot(Long roomId, Long scheduleId);
}
