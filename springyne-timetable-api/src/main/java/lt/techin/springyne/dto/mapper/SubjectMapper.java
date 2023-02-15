package lt.techin.springyne.dto.mapper;

import lt.techin.springyne.dto.SubjectDto;
import lt.techin.springyne.model.Subject;

public class SubjectMapper {

    public static Subject toSubject(SubjectDto subjectDto) {

        Subject subject = new Subject();

        subject.setName(subjectDto.getName());
        subject.setDescription(subjectDto.getDescription());
//        subject.setModule(subjectDto.getModule());
//        subject.setRooms(subjectDto.getRooms());

        return subject;
    }

    public static SubjectDto toSubjectDto(Subject subject) {

        SubjectDto subjectDto = new SubjectDto();

        subjectDto.setName(subject.getName());
        subjectDto.setDescription(subject.getDescription());
//        subjectDto.setModule(subject.getModule());
//        subjectDto.setRooms(subject.getRooms());

        return subjectDto;
    }


}
