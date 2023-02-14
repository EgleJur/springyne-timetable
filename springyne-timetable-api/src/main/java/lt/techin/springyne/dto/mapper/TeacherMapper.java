package lt.techin.springyne.dto.mapper;

import lt.techin.springyne.dto.TeacherDto;
import lt.techin.springyne.model.Teacher;

public class TeacherMapper {

    public static Teacher toTeacher(TeacherDto teacherDto) {

        Teacher teacher = new Teacher();
        teacher.setNumber(teacherDto.getNumber());
        teacher.setName(teacherDto.getName());
        teacher.setLastname(teacherDto.getLastname());
        teacher.setDeleted(teacherDto.isDeleted());

        return teacher;
    }

    public static TeacherDto toTeacherDto(Teacher teacher) {

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setNumber(teacher.getNumber());
        teacherDto.setName(teacher.getName());
        teacherDto.setLastname(teacher.getLastname());
        teacherDto.setDeleted(teacher.isDeleted());

        return teacherDto;
    }
}
