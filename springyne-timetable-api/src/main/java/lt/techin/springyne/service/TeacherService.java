package lt.techin.springyne.service;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.model.Teacher;
import lt.techin.springyne.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    private static final ExampleMatcher SEARCH_CONTAINS_NAME = ExampleMatcher.matchingAny()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withIgnorePaths("id", "number","deleted","modifiedDate");


    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }


    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAllByOrderByDeletedAscIdAsc();
    }

    public Teacher addTeacher(Teacher teacher) {
        if (existsByNumber(teacher.getNumber())) {
            throw new ScheduleValidationException("Teacher number must be unique", "number", "Number already exists", teacher.getNumber());
        }
        return teacherRepository.save(teacher);
    }

    public boolean existsByNumber(String number) {
        return teacherRepository.existsByNumberIgnoreCase(number);
    }

    public Page<Teacher> searchByNameAndSubjectAndShift(String name, String subject, String shift, int page, int pageSize) {
        Teacher teacher = new Teacher();
        if (name != null) {
            teacher.setName(name);
        }
        if (subject != null) {
            teacher.setSubject(subject);
        }
        if (shift != null) {
            teacher.setShift(shift);
        }
        Example<Teacher> teacherExample = Example.of(teacher, ExampleMatcher.matchingAll().withIgnoreCase("name", "subject", "shift").withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("deleted").and(Sort.by("id")));
        return teacherRepository.findAll(teacherExample, pageable);
    }

    public Optional<Teacher> getTeacherById(Long teacherId) {
        return teacherRepository.findById(teacherId);
    }

    public Teacher deleteTeacher(long teacherId) {
        Teacher teacherToDelete = teacherRepository.findById(teacherId).orElseThrow(
                () -> new ScheduleValidationException("Teacher does not exist", "id", "Teacher not found", String.valueOf(teacherId)));
        if (!teacherToDelete.isDeleted()) {
            teacherToDelete.setDeleted(true);
            return teacherRepository.save(teacherToDelete);
        } else {
            return teacherToDelete;
        }
    }

    public Teacher restoreTeacher(long teacherId) {
        Teacher teacherToRestore = teacherRepository.findById(teacherId).orElseThrow(
                () -> new ScheduleValidationException("Teacher does not exist", "id", "Teacher not found", String.valueOf(teacherId)));
        if (teacherToRestore.isDeleted()) {
            teacherToRestore.setDeleted(false);
            return teacherRepository.save(teacherToRestore);
        } else {
            return teacherToRestore;
        }
    }

    public Teacher updateTeacher(long teacherId, Teacher teacher) {
        Teacher updatedTeacher = teacherRepository.findById(teacherId).orElseThrow(
                () -> new ScheduleValidationException("Teacher does not exist", "id", "Teacher not found", String.valueOf(teacherId)));
        if (!updatedTeacher.getNumber().equals(teacher.getNumber())) {
            if(updatedTeacher.getNumber().equalsIgnoreCase(teacher.getNumber())) {
                updatedTeacher.setNumber(teacher.getNumber());
            } else if (teacherRepository.existsByNumberIgnoreCase(teacher.getNumber())) {
                throw new ScheduleValidationException("Teacher number must be unique", "number", "Number already exists", teacher.getNumber());
            } else {
                updatedTeacher.setNumber(teacher.getNumber());
            }
        }
        if (!updatedTeacher.getName().equals(teacher.getName())) {
            if (teacher.getName().equals("") || teacher.getName() == null) {
                throw new ScheduleValidationException("Teacher name cannot be empty", "name", "Name is empty", teacher.getName());
            } else {
                updatedTeacher.setName(teacher.getName());
            }
        }
        if (!updatedTeacher.getLastname().equals(teacher.getLastname())) {
            if (teacher.getLastname().equals("") || teacher.getLastname() == null) {
                throw new ScheduleValidationException("Teacher lastname cannot be empty", "lastname", "Lastname is empty", teacher.getLastname());
            } else {
                updatedTeacher.setLastname(teacher.getLastname());
            }
        }
//        if (updatedTeacher.isDeleted() != teacher.isDeleted()) {
//            updatedTeacher.setDeleted(teacher.isDeleted());
//        }
        return teacherRepository.save(updatedTeacher);
    }
}