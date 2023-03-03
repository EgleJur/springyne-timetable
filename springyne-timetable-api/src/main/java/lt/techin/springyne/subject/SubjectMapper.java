package lt.techin.springyne.subject;

public class SubjectMapper {

    public static Subject toSubject(SubjectDto subjectDto) {

        Subject subject = new Subject();

        subject.setName(subjectDto.getName());
        subject.setDescription(subjectDto.getDescription());

        return subject;
    }

    public static SubjectDto toSubjectDto(Subject subject) {

        SubjectDto subjectDto = new SubjectDto();

        subjectDto.setName(subject.getName());
        subjectDto.setDescription(subject.getDescription());

        return subjectDto;
    }


}