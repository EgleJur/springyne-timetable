package lt.techin.springyne.dto.mapper;

import lt.techin.springyne.dto.TeacherDto;
import lt.techin.springyne.model.Teacher;

public class TeacherMapper {

    public static Teacher toTeacher(TeacherDto teacherDto) {

        Teacher teacher = new Teacher();
        teacher.setName(teacherDto.getName());
        teacher.setEmail(teacherDto.getEmail());
        teacher.setTeamsEmail(teacherDto.getTeamsEmail());
        teacher.setPhone(teacherDto.getPhone());
        teacher.setHours(teacherDto.getHours());
        teacher.setDeleted(teacherDto.isDeleted());

        return teacher;
    }

    public static TeacherDto toTeacherDto(Teacher teacher) {

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setName(teacher.getName());
        teacherDto.setTeamsEmail(teacher.getTeamsEmail());
        teacherDto.setEmail(teacher.getEmail());
        teacherDto.setPhone(teacher.getPhone());
        teacherDto.setHours(teacher.getHours());
        teacherDto.setDeleted(teacher.isDeleted());

        return teacherDto;
    }
}
