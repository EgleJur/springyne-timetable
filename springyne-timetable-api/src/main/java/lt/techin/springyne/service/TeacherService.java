package lt.techin.springyne.service;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.model.Shift;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.model.Teacher;
import lt.techin.springyne.repository.ShiftRepository;
import lt.techin.springyne.repository.SubjectRepository;
import lt.techin.springyne.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    SubjectRepository subjectRepository;

    public TeacherService(TeacherRepository teacherRepository, ShiftRepository shiftRepository, SubjectRepository subjectRepository) {
        this.teacherRepository = teacherRepository;
        this.shiftRepository = shiftRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAllByOrderByDeletedAscIdAsc();
    }

    public Teacher addTeacher(Long shiftId, Long subjectId, Teacher teacher) {
        if (teacher.getName() == null || teacher.getName().equals("")) {
            throw new ScheduleValidationException("Teacher name cannot be empty", "name", "Name is empty", teacher.getName());
        }
        if (shiftId != null) {
            Shift shiftToAdd = shiftRepository.findById(shiftId).orElseThrow(() -> new ScheduleValidationException("Shift does not exist", "id",
                    "Shift not found", String.valueOf(shiftId)));
            teacher.setShift(shiftToAdd);
        }
        if(subjectId != null) {
            Subject subjectToAdd = subjectRepository.findById(subjectId).orElseThrow(() -> new ScheduleValidationException("Subject does not exist", "id",
                    "Subject not found", String.valueOf(subjectId)));
            teacher.getSubjects().add(subjectToAdd);
        }

        return teacherRepository.save(teacher);
    }

    public Page<Teacher> searchByNameShiftSubjectPaged(String name, Long shiftId, Long subjectId, int page, int pageSize) {
        if (name==null) {
            name = "";
        }
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by("deleted").and(Sort.by("id")));
        if (shiftId == null && subjectId == null) {
            return teacherRepository.findAllByNameIgnoreCaseContaining(name,pageable);
        }
        if (shiftId == null) {
            return teacherRepository.findAllByNameIgnoreCaseContainingAndSubjects_Id(name,subjectId,pageable);
        }
        if (subjectId == null) {
            return teacherRepository.findAllByNameIgnoreCaseContainingAndShiftId(name, shiftId, pageable);
        }
        return teacherRepository.findAllByNameIgnoreCaseContainingAndShiftIdAndSubjects_Id(name, shiftId, subjectId, pageable);
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

    public Teacher updateTeacher(long teacherId, Teacher teacher, Long shiftId, Long subjectId) {
        Teacher updatedTeacher = teacherRepository.findById(teacherId).orElseThrow(
                () -> new ScheduleValidationException("Teacher does not exist", "id", "Teacher not found", String.valueOf(teacherId)));

            if (teacher.getName().equals("") || teacher.getName() == null) {
                throw new ScheduleValidationException("Teacher name cannot be empty", "name", "Name is empty", teacher.getName());
            } else {
                updatedTeacher.setName(teacher.getName());
            }
        if (!teacher.getEmail().equals(updatedTeacher.getEmail())) {
            if (!(teacher.getEmail().equals("") || teacher.getEmail() == null)) {
                updatedTeacher.setEmail(teacher.getEmail());
            }
        }
        if (!teacher.getTeamsEmail().equals(updatedTeacher.getTeamsEmail())) {
            if (!(teacher.getTeamsEmail().equals("") || teacher.getTeamsEmail() == null)) {
                updatedTeacher.setTeamsEmail(teacher.getTeamsEmail());
            }
        }
        if (!teacher.getPhone().equals(updatedTeacher.getPhone())) {
            if (!(teacher.getPhone().equals("") || teacher.getPhone() == null)) {
                updatedTeacher.setPhone(teacher.getPhone());
            }
        }
        if (!teacher.getHours().equals(updatedTeacher.getHours())) {
            if (!(teacher.getHours() == null)) {
                updatedTeacher.setHours(teacher.getHours());
            }
        }

        if(shiftId != null) {
            Shift shift = shiftRepository.findById(shiftId).orElseThrow(() -> new ScheduleValidationException("Shift does not exist", "shiftId",
                    "Shift not found", String.valueOf(shiftId)));
            updatedTeacher.setShift(shift);
        }
        if (subjectId != null) {
            Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new ScheduleValidationException("Subject does not exist", "id",
                    "Subject not found", String.valueOf(subjectId)));
            updatedTeacher.getSubjects().add(subject);
        }

        return teacherRepository.save(updatedTeacher);
    }

    public Teacher removeSubjectFromTeacher(Long teacherId, Long subjectId) {
        Teacher teacherToUpdate = teacherRepository.findById(teacherId).orElseThrow(() -> new ScheduleValidationException("Teacher does not exist", "teacherId",
                "Teacher not found", String.valueOf(teacherId)));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist", "subjectId", "Subject not found",
                        String.valueOf(subjectId)));
        teacherToUpdate.getSubjects().remove(subject);
        return teacherRepository.save(teacherToUpdate);
    }
}