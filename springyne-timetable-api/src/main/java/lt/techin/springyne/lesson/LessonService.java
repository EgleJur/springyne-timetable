package lt.techin.springyne.lesson;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.holiday.Holiday;
import lt.techin.springyne.holiday.HolidaysRepository;
import lt.techin.springyne.room.Room;
import lt.techin.springyne.room.RoomRepository;
import lt.techin.springyne.schedule.Schedule;
import lt.techin.springyne.schedule.ScheduleRepository;
import lt.techin.springyne.subject.Subject;
import lt.techin.springyne.subject.SubjectRepository;
import lt.techin.springyne.teacher.Teacher;
import lt.techin.springyne.teacher.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new ScheduleValidationException("Schedule does not exist",
                "schedule id", "Schedule not found", scheduleId.toString()));

        if (lessonBlock.getStartDate() == null || lessonBlock.getEndDate() == null || lessonBlock.getEndDate().isBefore(lessonBlock.
                getStartDate()) || lessonBlock.getStartDate().isBefore(schedule.getStartDate()) || lessonBlock.getStartDate().
                isAfter(schedule.getEndDate()) || lessonBlock.getEndDate().isBefore(schedule.getStartDate()) || lessonBlock.getEndDate()
                .isAfter(schedule.getEndDate())) {
            throw new ScheduleValidationException("Invalid lesson date range", "lesson date range",
                    "Lesson date is invalid", lessonBlock.getStartDate().toString() + " - " + lessonBlock.getEndDate());
        }
        if (lessonBlock.getStartTime() == null || lessonBlock.getEndTime() == null || lessonBlock.getStartTime() < lessonBlock.getEndTime()
        || lessonBlock.getStartTime() < 1 || lessonBlock.getStartTime() > 14 || lessonBlock.getEndTime() < 1 || lessonBlock.getEndTime() > 14
        || lessonBlock.getStartTime() < schedule.getGroup().getShift().getStarts() || lessonBlock.getEndTime() > schedule.getGroup().getShift()
                .getEnds() || lessonBlock.getStartTime() > schedule.getGroup().getShift().getEnds() || lessonBlock.getEndTime() <
                schedule.getGroup().getShift().getStarts()) {
            throw new ScheduleValidationException("Invalid lesson time range", "lesson time",
                    "Lesson time is invalid", lessonBlock.getStartTime().toString() + " - " + lessonBlock.getEndTime());
        }

        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                "subject id", "Subject not found", subjectId.toString()));
        if (schedule.getGroup().getProgram().getSubjects().stream().noneMatch(
                programSubject -> programSubject.getSubject().equals(subject))) {
            throw new ScheduleValidationException("Subject is not covered by this program", "subject id",
                    "Subject is invalid", subjectId.toString());
        }

        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new ScheduleValidationException("Teacher does not exist",
                "teacher id", "Teacher not found", teacherId.toString()));
        if (!teacher.getSubjects().contains(subject)) {
            throw new ScheduleValidationException("Teacher does not teach this subject", "teacher id",
                    "Teacher is invalid", subjectId.toString());
        }

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ScheduleValidationException("Room does not exist",
                "room id", "Room not found", roomId.toString()));
        if (!subject.getRooms().contains(room)) {
            throw new ScheduleValidationException("Subject cannot be taught in this room", "room id",
                    "Room is invalid", subjectId.toString());
        }

        List<Lesson> existingLessons = lessonRepository.findByLessonDateBetweenAndLessonTimeBetween(lessonBlock.getStartDate(),
                lessonBlock.getEndDate(),lessonBlock.getStartTime(),lessonBlock.getEndTime());

        if (existingLessons.stream().anyMatch(lesson -> lesson.getTeacher().equals(teacher))) {
            throw new ScheduleValidationException("Teacher time is already reserved in this period", "teacher id",
                    "Teacher is busy", teacherId.toString());
        }

        if (existingLessons.stream().anyMatch(lesson -> lesson.getRoom().equals(room))) {
            throw new ScheduleValidationException("Room is already reserved in this period", "room id",
                    "Room is occupied", roomId.toString());
        }

        List<Holiday> holidays = holidaysRepository.findAllHolidaysByDate(schedule.getStartDate(), schedule.getEndDate());

        List<LocalDate> holidayDateList = holidays.stream().flatMap(holiday -> holiday.getStarts().datesUntil(holiday.getEnds().plusDays(1))).collect(Collectors.toList());

        List <Lesson> lessons = new ArrayList<>();
        for (LocalDate i = lessonBlock.getStartDate(); i.isAfter(lessonBlock.getEndDate()); i = i.plusDays(1)) {
            if (holidayDateList.contains(i)) {
                continue;
            }
            for (int j = lessonBlock.getStartTime(); j<=lessonBlock.getEndTime(); j++) {
                Lesson newLesson = new Lesson();
                newLesson.setLessonDate(i);
                newLesson.setLessonTime(j);
                newLesson.setRoom(room);
                newLesson.setTeacher(teacher);
                newLesson.setSubject(subject);
                lessons.add(newLesson);
            }
        }


        return lessonRepository.saveAll(lessons);
    }
}
