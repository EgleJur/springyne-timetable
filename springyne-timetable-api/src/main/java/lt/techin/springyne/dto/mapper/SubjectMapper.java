package lt.techin.springyne.dto.mapper;

import lt.techin.springyne.dto.SubjectDto;
import lt.techin.springyne.model.Module;
import lt.techin.springyne.model.Subject;

import java.util.Set;

public class SubjectMapper {

    public static Subject toSubject(SubjectDto subjectDto) {

        Subject subject = new Subject();

       // subject.setId(subjectDto.getId());
        subject.setName(subjectDto.getName());
        subject.setDescription(subjectDto.getDescription());
        //subject.setModule((Set<Module>) subjectDto.getModule());
       // subject.setRooms((Set<Room>) subjectDto.getRoom());

        return subject;
    }

    public static SubjectDto toSubjectDto(Subject subject) {

        SubjectDto subjectDto = new SubjectDto();

     //   subjectDto.setId(subject.getId());
        subjectDto.setName(subject.getName());
        subjectDto.setDescription(subject.getDescription());
        subjectDto.setModule((Set<Module>) subject.getModule());
        subjectDto.setRoom((Set<Room>) subject.getRooms());

        return subjectDto;
    }


}
