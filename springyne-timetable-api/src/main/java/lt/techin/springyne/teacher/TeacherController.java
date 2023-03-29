package lt.techin.springyne.teacher;

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
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @PostMapping
    public ResponseEntity<Teacher> addTeacher(@Valid @RequestBody TeacherDto teacherDto,
                                              @RequestParam Long shiftId,
                                              @RequestParam(required = false) Long subjectId) {
        return ResponseEntity.ok(teacherService.addTeacher(shiftId, subjectId, TeacherMapper.toTeacher(teacherDto)));
    }

    @GetMapping("/search")
    public Page<Teacher> filterTeachersByNameShiftSubjectPaged(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long shiftId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam int page,
            @RequestParam int pageSize) {

        return teacherService.searchByNameShiftSubjectPaged(name, shiftId, subjectId, page, pageSize);
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
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long teacherId, @Valid @RequestBody TeacherDto teacherDto,
                                                 @RequestParam(required = false) Long shiftId,
                                                 @RequestParam(required = false) Long subjectId) {
        return ResponseEntity.ok(teacherService.updateTeacher(teacherId, TeacherMapper.toTeacher(teacherDto), shiftId, subjectId));
    }

    @PatchMapping("/{teacherId}/removeSubject/{subjectId}")
    public ResponseEntity<Teacher> removeSubjectFromTeacher(@PathVariable Long teacherId, @PathVariable Long subjectId) {
        return ResponseEntity.ok(teacherService.removeSubjectFromTeacher(teacherId,subjectId));
    }

    @GetMapping("/subject")
    public List<Teacher> getTeachersBySubjectIdAndAvailableHours(@RequestParam(required = false) Long subjectId,
                                                                 @RequestParam(required = false) Integer startTime,
                                                                 @RequestParam(required = false) Integer endTime) {
        return teacherService.getAvailableTeachersBySubjectId(subjectId, startTime, endTime);
    }

}
