package lt.techin.springyne.lesson;

import lombok.extern.slf4j.Slf4j;
import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.holiday.Holiday;
import lt.techin.springyne.holiday.HolidaysRepository;
import lt.techin.springyne.program.ProgramSubject;
import lt.techin.springyne.room.Room;
import lt.techin.springyne.room.RoomRepository;
import lt.techin.springyne.schedule.Schedule;
import lt.techin.springyne.schedule.ScheduleRepository;
import lt.techin.springyne.shift.Shift;
import lt.techin.springyne.subject.Subject;
import lt.techin.springyne.subject.SubjectRepository;
import lt.techin.springyne.teacher.Teacher;
import lt.techin.springyne.teacher.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LessonService {

    @Autowired
    private final LessonRepository lessonRepository;
    @Autowired
    private final ScheduleRepository scheduleRepository;
    @Autowired
    private final SubjectRepository subjectRepository;
    @Autowired
    private final TeacherRepository teacherRepository;
    @Autowired
    private final RoomRepository roomRepository;
    @Autowired
    private final HolidaysRepository holidaysRepository;


    public LessonService(LessonRepository lessonRepository, ScheduleRepository scheduleRepository, SubjectRepository subjectRepository,
                         TeacherRepository teacherRepository, RoomRepository roomRepository, HolidaysRepository holidaysRepository) {
        this.lessonRepository = lessonRepository;
        this.scheduleRepository = scheduleRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.roomRepository = roomRepository;
        this.holidaysRepository = holidaysRepository;
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public Optional<Lesson> getLessonById(Long lessonId) {
        return lessonRepository.findById(lessonId);
    }

    public List<Lesson> getLessonsBySchedule(Long scheduleId) {
        return lessonRepository.findByScheduleIdOrderByLessonDateAscLessonTimeAsc(scheduleId);
    }

    public List<Lesson> addLesson(LessonBlock lessonBlock, Long scheduleId, Long subjectId, Long teacherId, Long roomId) {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleValidationException("Schedule does not exist",
                        "schedule id", "Schedule not found", scheduleId.toString()));

        if (lessonBlock.getStartDate() == null || lessonBlock.getEndDate() == null || lessonBlock.getEndDate().isBefore(lessonBlock.
                getStartDate()) || lessonBlock.getStartDate().isBefore(schedule.getStartDate()) || lessonBlock.getStartDate().
                isAfter(schedule.getEndDate()) || lessonBlock.getEndDate().isBefore(schedule.getStartDate()) || lessonBlock.getEndDate()
                .isAfter(schedule.getEndDate())) {
            throw new ScheduleValidationException("Invalid lesson date range", "lesson date range",
                    "Lesson date is invalid", lessonBlock.getStartDate().toString() + " - " + lessonBlock.getEndDate());
        }
        if (lessonBlock.getStartTime() == null || lessonBlock.getEndTime() == null || lessonBlock.getStartTime() > lessonBlock.getEndTime()
                || lessonBlock.getStartTime() < 1 || lessonBlock.getStartTime() > 14 || lessonBlock.getEndTime() < 1 || lessonBlock.getEndTime() > 14
                || lessonBlock.getStartTime() < schedule.getGroup().getShift().getStarts() || lessonBlock.getEndTime() > schedule.getGroup().getShift()
                .getEnds() || lessonBlock.getStartTime() > schedule.getGroup().getShift().getEnds() || lessonBlock.getEndTime() <
                schedule.getGroup().getShift().getStarts()) {
            throw new ScheduleValidationException("Invalid lesson time range", "lesson time",
                    "Lesson time is invalid", lessonBlock.getStartTime().toString() + " - " + lessonBlock.getEndTime());
        }

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                        "subject id", "Subject not found", subjectId.toString()));

        ProgramSubject lessonProgramSubject = schedule.getGroup().getProgram().getSubjects().stream().filter(programSubject ->
                programSubject.getSubject().equals(subject)).findFirst().orElseThrow(() -> new ScheduleValidationException(
                "Subject is not covered by this program", "subject id", "Subject is invalid", subjectId.toString()));

        List<Lesson> existingLessons = lessonRepository.findByLessonDateBetweenAndLessonTimeBetween(lessonBlock.getStartDate(),
                lessonBlock.getEndDate(), lessonBlock.getStartTime(), lessonBlock.getEndTime());

        Teacher teacher;

        if (teacherId != null) {
            teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new ScheduleValidationException("Teacher does not exist",
                    "teacher id", "Teacher not found", teacherId.toString()));
            if (!teacher.getSubjects().contains(subject)) {
                throw new ScheduleValidationException("Teacher does not teach this subject", "teacher id",
                        "Teacher is invalid", subjectId.toString());
            }
            if (!((lessonBlock.getStartTime() >= teacher.getShift().getStarts()) && (lessonBlock.getEndTime() <= teacher.getShift().getEnds()))) {
                throw new ScheduleValidationException("Teacher does not teach on these hours", "teacher id",
                        "Teacher is invalid", subjectId.toString());
            }
            if (existingLessons.stream().anyMatch(lesson -> lesson.getTeacher().equals(teacher))) {
                throw new ScheduleValidationException("Teacher time is already reserved in this period", "teacher id",
                        "Teacher is busy", teacherId.toString());
            }
        } else {
            teacher = null;
        }

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ScheduleValidationException("Room does not exist",
                "room id", "Room not found", roomId.toString()));
        if (!subject.getRooms().contains(room)) {
            throw new ScheduleValidationException("Subject cannot be taught in this room", "room id",
                    "Room is invalid", subjectId.toString());
        }

        if (existingLessons.stream().anyMatch(lesson -> lesson.getRoom().equals(room))) {
            throw new ScheduleValidationException("Room is already reserved in this period", "room id",
                    "Room is occupied", roomId.toString());
        }

        if (existingLessons.stream().anyMatch(lesson -> lesson.getSchedule().equals(schedule))) {
            throw new ScheduleValidationException("Schedule is already planned in this period", "schedule id",
                    "Group is busy", scheduleId.toString());
        }

        List<Holiday> holidays = holidaysRepository.findAllHolidaysByDate(schedule.getStartDate(), schedule.getEndDate());

        List<LocalDate> holidayDateList = holidays.stream().flatMap(holiday -> holiday.getStarts().datesUntil(holiday.getEnds()
                .plusDays(1))).collect(Collectors.toList());

        List<Lesson> lessons = new ArrayList<>();
        for (LocalDate i = lessonBlock.getStartDate(); !i.isAfter(lessonBlock.getEndDate()); i = i.plusDays(1)) {
            if (holidayDateList.contains(i) || i.getDayOfWeek().equals(DayOfWeek.SATURDAY) || i.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                continue;
            }
            for (int j = lessonBlock.getStartTime(); j <= lessonBlock.getEndTime(); j++) {
                Lesson newLesson = new Lesson();
                newLesson.setLessonDate(i);
                newLesson.setLessonTime(j);
                newLesson.setRoom(room);
                newLesson.setTeacher(teacher);
                newLesson.setSubject(subject);
                newLesson.setSchedule(schedule);
                lessons.add(newLesson);
            }
        }

        Long remainingLessons = lessonProgramSubject.getHours() - lessonRepository.countBySubjectIdAndSchedule_GroupId(subjectId,
                schedule.getGroup().getId());
        log.info(remainingLessons.toString());
        if (lessons.size() > remainingLessons) {
            throw new ScheduleValidationException("Number of lessons is greater than covered by program", "subject id",
                    "Subject is overbooked", lessonBlock.getStartDate().toString() + " - " + lessonBlock.getEndDate());
        }
        if (teacher != null) {
            List<Lesson> existingTeacherLessons = lessonRepository.findAllByTeacherId(teacherId);
            existingTeacherLessons.addAll(lessons);
            Map<LocalDate, Long> teacherLessonCountPerWeek = existingTeacherLessons.stream().collect(Collectors.groupingBy(lesson -> lesson.getLessonDate()
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)), Collectors.counting()));
            boolean isTeacherWeekOverbooked = teacherLessonCountPerWeek.entrySet().stream().anyMatch(entry -> entry.getValue() > teacher.getHours());
            if (isTeacherWeekOverbooked) {
                throw new ScheduleValidationException("Number of lessons per week greater than teacher week working hours", "teacher id",
                        "Teacher is overbooked", teacherId.toString());
            }
        }

        if (lessons.size() == 0) {
            throw new ScheduleValidationException("No lessons meet validation requirements", "lessonDate",
                    "No lessons were created", lessonBlock.getStartDate().toString() + " - " + lessonBlock.getEndDate().toString());
        }

        return lessonRepository.saveAll(lessons);
    }

    public List<Lesson> editSingleLesson(Long lessonId, Long subjectId, Long teacherId, Long roomId) {

        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ScheduleValidationException("Lesson does not exist",
                        "lessonId", "Lesson not found", String.valueOf(lessonId)));

        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                "subject id", "Subject not found", subjectId.toString()));

        List<Lesson> lessonsSameDay = lessonRepository.findAllByLessonDateAndSubjectIdAndScheduleId(existingLesson.getLessonDate(), subjectId,
                existingLesson.getSchedule().getId());

        Teacher teacher;

        if (teacherId != null) {
            teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new ScheduleValidationException("Teacher does not exist",
                            "teacher id", "Teacher not found", teacherId.toString()));

            if (!teacher.getSubjects().contains(subject)) {
                throw new ScheduleValidationException("Teacher does not teach this subject", "teacher id",
                        "Teacher is invalid", subjectId.toString());
            }

            List<Lesson> existingTeacherLessons = lessonRepository.findAllByTeacherIdAndLessonDateAndScheduleIdNot(teacherId,
                    existingLesson.getLessonDate(), existingLesson.getSchedule().getId());
            if (!existingTeacherLessons.isEmpty()) {
                for (Lesson lesson : lessonsSameDay) {
                    boolean isTeacherBusy = existingTeacherLessons.stream().anyMatch(teacherLesson -> teacherLesson.getLessonTime().equals(lesson.getLessonTime()));
                    if (isTeacherBusy) {
                        throw new ScheduleValidationException("Teacher time is already reserved in this period", "teacher id",
                                "Teacher is busy", teacherId.toString());
                    }
                }
            }
        } else {
            teacher = null;
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ScheduleValidationException("Room does not exist",
                        "room id", "Room not found", roomId.toString()));

        if (!subject.getRooms().contains(room)) {
            throw new ScheduleValidationException("Subject cannot be taught in this room", "room id",
                    "Room is invalid", subjectId.toString());
        }

        List<Lesson> existingRoomLessons = lessonRepository.findAllByRoomIdAndLessonDateAndScheduleIdNot(roomId, existingLesson.getLessonDate(),
                existingLesson.getSchedule().getId());
        if (!existingRoomLessons.isEmpty()) {
            for (Lesson lesson : lessonsSameDay) {
                boolean isRoomBusy = existingRoomLessons.stream().anyMatch(roomLesson -> roomLesson.getLessonTime().equals(lesson.getLessonTime()));
                if (isRoomBusy) {
                    throw new ScheduleValidationException("Room is already reserved in this period", "room id",
                            "Room is busy", roomId.toString());
                }
            }
        }

        for (Lesson lesson : lessonsSameDay) {
            lesson.setTeacher(teacher);
            lesson.setRoom(room);
        }

        return lessonRepository.saveAll(lessonsSameDay);
    }

    public List<Lesson> editMultipleLessons(Long scheduleId, Long subjectId, Long teacherId, Long roomId) {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleValidationException("Schedule does not exist",
                        "schedule id", "Schedule not found", scheduleId.toString()));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                        "subject id", "Subject not found", subjectId.toString()));

        Shift shift = schedule.getGroup().getShift();
        List<Lesson> lessons = lessonRepository.findAllBySubjectIdAndScheduleId(subjectId, scheduleId);

        Teacher teacher;

        if (teacherId != null) {
            teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new ScheduleValidationException("Teacher does not exist",
                            "teacher id", "Teacher not found", teacherId.toString()));
            if (!teacher.getSubjects().contains(subject)) {
                throw new ScheduleValidationException("Teacher does not teach this subject", "teacher id",
                        "Teacher is invalid", subjectId.toString());
            }
            if (!((shift.getStarts() >= teacher.getShift().getStarts()) && (shift.getEnds() <= teacher.getShift().getEnds()))) {
                throw new ScheduleValidationException("Teacher does not teach on these hours", "teacher id",
                        "Teacher is invalid", subjectId.toString());
            }
            List<Lesson> existingTeacherLessons = lessonRepository.findAllByTeacherIdAndScheduleIdNot(teacherId, scheduleId);

            if (!existingTeacherLessons.isEmpty()) {
                for (Lesson lesson : lessons) {
                    boolean isTeacherBusy = existingTeacherLessons.stream().anyMatch(teacherLesson -> teacherLesson.getLessonDate()
                            .equals(lesson.getLessonDate()) && teacherLesson.getLessonTime().equals(lesson.getLessonTime()));
                    if (isTeacherBusy) {
                        throw new ScheduleValidationException("Teacher time is already reserved in this period", "teacher id",
                                "Teacher is busy", teacherId.toString());
                    }
                }
            }

        } else {
            teacher = null;
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ScheduleValidationException("Room does not exist",
                        "room id", "Room not found", roomId.toString()));

        if (!subject.getRooms().contains(room)) {
            throw new ScheduleValidationException("Subject cannot be taught in this room", "room id",
                    "Room is invalid", subjectId.toString());
        }

        List<Lesson> existingRoomLessons = lessonRepository.findAllByRoomIdAndScheduleIdNot(roomId, scheduleId);
        if (!existingRoomLessons.isEmpty()) {
            for (Lesson lesson : lessons) {
                boolean isRoomBusy = existingRoomLessons.stream().anyMatch(roomLesson -> roomLesson.getLessonDate()
                                .equals(lesson.getLessonDate()) && roomLesson.getLessonTime().equals(lesson.getLessonTime()));
                if (isRoomBusy) {
                    throw new ScheduleValidationException("Room is already reserved in this period", "room id",
                            "Room is busy", roomId.toString());
                }
            }
        }
        for (Lesson lesson : lessons) {
            lesson.setRoom(room);
            lesson.setTeacher(teacher);
        }

        return lessonRepository.saveAll(lessons);
    }

    public boolean deleteLessonsByDateAndId(Long lessonId) {
        Optional<Lesson> lesson = lessonRepository.findById(lessonId);
        if (lesson.isPresent()) {
            long subjectId = lesson.get().getSubject().getId();
            long scheduleId = lesson.get().getSchedule().getId();
            LocalDate lessonDate = lesson.get().getLessonDate();
            List<Lesson> lessonsToDelete = lessonRepository.findAllByLessonDateAndSubjectIdAndScheduleId(lessonDate, subjectId, scheduleId);
            lessonRepository.deleteAll(lessonsToDelete);
            return true;
        } else {
            return false;
        }
    }

    public List<Lesson> listTeacherLessons(Long teacherId, String from, String to){
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);
        return lessonRepository.findAllByTeacherIdAndLessonDateBetweenOrderByLessonDateAscLessonTimeAsc(teacherId, startDate, endDate);
    }

    public List<Lesson> listRoomLessons(Long roomId, String from, String to) {
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);
        return lessonRepository.findAllByRoomIdAndLessonDateBetweenOrderByLessonDateAscLessonTimeAsc(roomId, startDate, endDate);
    }
}


