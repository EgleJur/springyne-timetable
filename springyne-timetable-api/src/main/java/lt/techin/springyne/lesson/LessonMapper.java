package lt.techin.springyne.lesson;

public class LessonMapper {

    public static Lesson toLesson(LessonDto lessonDto) {

        Lesson lesson = new Lesson();
        lesson.setLessonDate(lessonDto.getLessonDate());
        lesson.setLessonTime(lessonDto.getLessonTime());

        return lesson;
    }

    public static LessonDto toLessonDto (Lesson lesson) {

        LessonDto lessonDto = new LessonDto();
        lessonDto.setLessonTime(lesson.getLessonTime());
        lessonDto.setLessonDate(lesson.getLessonDate());

        return lessonDto;
    }
}
