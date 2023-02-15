package lt.techin.springyne.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.dto.TeacherDto;
import lt.techin.springyne.dto.mapper.TeacherMapper;
import lt.techin.springyne.model.Teacher;
import lt.techin.springyne.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/teachers")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @Autowired
    ObjectMapper objectMapper;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @PostMapping
    public ResponseEntity<Teacher> addTeacher(@Valid @RequestBody TeacherDto teacherDto) {
        return ResponseEntity.ok(teacherService.addTeacher(TeacherMapper.toTeacher(teacherDto)));
    }

    @GetMapping("/search")
    public Page<Teacher> filterTeachersByNamePaged(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String shift,
            @RequestParam int page,
            @RequestParam int pageSize) {

        return teacherService.searchByNameAndSubjectAndShift(name, subject, shift, page, pageSize);
    }

    @GetMapping("/{teacherId}")
    public Optional<Teacher> getTeacherById(@PathVariable Long teacherId) {
        return teacherService.getTeacherById(teacherId);
    }

    @PatchMapping("/delete/{teacherId}")
    public ResponseEntity<Teacher> deleteTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(teacherService.deleteTeacher(teacherId));
    }

    @PatchMapping("/restore/{teacherId}")
    public ResponseEntity<Teacher> restoreTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(teacherService.restoreTeacher(teacherId));
    }

    @PatchMapping("/update/{teacherId}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long teacherId, @Valid @RequestBody TeacherDto teacherDto) {
        return ResponseEntity.ok(teacherService.updateTeacher(teacherId, TeacherMapper.toTeacher(teacherDto)));
    }

}
