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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HolidaysRepository holidaysRepository;

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
        return lessonRepository.findByScheduleId(scheduleId);
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


        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new ScheduleValidationException("Teacher does not exist",
                "teacher id", "Teacher not found", teacherId.toString()));
        if (!teacher.getSubjects().contains(subject)) {
            throw new ScheduleValidationException("Teacher does not teach this subject", "teacher id",
                    "Teacher is invalid", subjectId.toString());
        }

//        if (!((lessonBlock.getStartTime() >= teacher.getShift().getStarts()) && (lessonBlock.getEndTime() <= teacher.getShift().getEnds()))) {

        if (!((lessonBlock.getStartTime() >= teacher.getShift().getStarts()) && (lessonBlock.getEndTime() <= teacher.getShift().getEnds()))) {

            throw new ScheduleValidationException("Teacher does not teach on these hours", "teacher id",
                    "Teacher is invalid", subjectId.toString());
        }

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ScheduleValidationException("Room does not exist",
                "room id", "Room not found", roomId.toString()));
        if (!subject.getRooms().contains(room)) {
            throw new ScheduleValidationException("Subject cannot be taught in this room", "room id",
                    "Room is invalid", subjectId.toString());
        }

        List<Lesson> existingLessons = lessonRepository.findByLessonDateBetweenAndLessonTimeBetween(lessonBlock.getStartDate(),
                lessonBlock.getEndDate(), lessonBlock.getStartTime(), lessonBlock.getEndTime());

        if (existingLessons.stream().anyMatch(lesson -> lesson.getTeacher().equals(teacher))) {
            throw new ScheduleValidationException("Teacher time is already reserved in this period", "teacher id",
                    "Teacher is busy", teacherId.toString());
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


        return lessonRepository.saveAll(lessons);
    }

    public Lesson editSingleLesson(Long lessonId, Long subjectId, Long teacherId, Long roomId) {

        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ScheduleValidationException("Lesson does not exist",
                        "lessonId", "Lesson not found", String.valueOf(lessonId)));

        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                "subject id", "Subject not found", subjectId.toString()));

        if (teacherId != null) {
            Teacher teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new ScheduleValidationException("Teacher does not exist",
                            "teacher id", "Teacher not found", teacherId.toString()));

            if (!teacher.getSubjects().contains(subject)) {
                throw new ScheduleValidationException("Teacher does not teach this subject", "teacher id",
                        "Teacher is invalid", subjectId.toString());
            }
            existingLesson.setTeacher(teacher);
        }
        if (roomId != null) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new ScheduleValidationException("Room does not exist",
                            "room id", "Room not found", roomId.toString()));

            if (!subject.getRooms().contains(room)) {
                throw new ScheduleValidationException("Subject cannot be taught in this room", "room id",
                        "Room is invalid", subjectId.toString());
            }
            existingLesson.setRoom(room);
        }
        return lessonRepository.save(existingLesson);
    }

    public List<Lesson> editMultipleLessons(Long scheduleId, Long subjectId, Long teacherId, Long roomId) {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleValidationException("Schedule does not exist",
                        "schedule id", "Schedule not found", scheduleId.toString()));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                        "subject id", "Subject not found", subjectId.toString()));


        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ScheduleValidationException("Teacher does not exist",
                        "teacher id", "Teacher not found", teacherId.toString()));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ScheduleValidationException("Room does not exist",
                        "room id", "Room not found", roomId.toString()));


        if (!teacher.getSubjects().contains(subject)) {
            throw new ScheduleValidationException("Teacher does not teach this subject", "teacher id",
                    "Teacher is invalid", subjectId.toString());
        }

        Shift shift = schedule.getGroup().getShift();

        if (!((shift.getStarts() == teacher.getShift().getStarts()) && (shift.getEnds() <= teacher.getShift().getEnds()))) {
            throw new ScheduleValidationException("Teacher does not teach on these hours", "teacher id",
                    "Teacher is invalid", subjectId.toString());
        }

        if (!subject.getRooms().contains(room)) {
            throw new ScheduleValidationException("Subject cannot be taught in this room", "room id",
                    "Room is invalid", subjectId.toString());
        }
        List<Lesson> lessons = lessonRepository.findAllBySubjectIdAndScheduleId(subjectId, scheduleId);
        for (Lesson lesson : lessons) {
            if (teacherId != null) {
                lesson.setTeacher(teacher);
                if (roomId != null) {
                    lesson.setRoom(room);
                }
            } else if (roomId != null) {
                lesson.setRoom(room);
            }
        }

        return lessonRepository.saveAll(lessons);
    }
}


