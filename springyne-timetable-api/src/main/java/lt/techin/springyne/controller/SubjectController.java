package lt.techin.springyne.controller;

import lt.techin.springyne.dto.SubjectDto;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static lt.techin.springyne.dto.mapper.SubjectMapper.toSubject;
import static lt.techin.springyne.dto.mapper.SubjectMapper.toSubjectDto;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/subjects")

public class SubjectController {

    @Autowired
    SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/search")
    public Page<Subject> searchByNamePaged(@RequestParam(required = false) String name,
                                           @RequestParam int page,
                                           @RequestParam int pageSize) {
        return subjectService.searchByNamePaged(name, page, pageSize);
    }

    @GetMapping("/byModule/search")

    public Page<Subject> getByModule(@RequestParam String name, @RequestParam int page,
                                     @RequestParam int pageSize) {
        return subjectService.getByModule(name, page, pageSize);
    }

    @GetMapping("/{subjectId}")
    public Optional<Subject> getSubject(@PathVariable Long subjectId) {
        return subjectService.getById(subjectId);
    }

    @PostMapping
    public ResponseEntity<SubjectDto> createSubject(@RequestBody SubjectDto subjectDto) {
        var createdSubject = subjectService.createSubjectDto(toSubject(subjectDto));

        return ok(toSubjectDto(createdSubject));
    }
    @PostMapping(value = "/createSubject")
    public ResponseEntity<SubjectDto> createSubject1(@RequestBody SubjectDto subjectDto,
                                                     @RequestParam Long moduleId,
                                                     @RequestParam(required = false) Long roomId) {
        var createdSubject = subjectService.createSubject1(moduleId, roomId, toSubject(subjectDto));
        return ok(toSubjectDto(createdSubject));
    }

    @PatchMapping("/test")
    public void subRoom(@RequestParam Long sudId, @RequestParam Long modId) {
        subjectService.subRoom(sudId, modId);
    }

    @PatchMapping("/edit/{subjectId}")
    public ResponseEntity<Subject> editSubject(@PathVariable Long subjectId,
                                               @RequestBody SubjectDto subjectDto) {
        var updatedSubject = subjectService.edit(subjectId, toSubject(subjectDto));

        return ok(updatedSubject);
    }


    @PatchMapping("/delete/{subjectId}")
    public ResponseEntity<SubjectDto> deleteSubject(@PathVariable Long subjectId) {

        var updatedSubject = subjectService.delete(subjectId);
        return ok(toSubjectDto(updatedSubject));

    }

    @PatchMapping("/restore/{subjectId}")
    public ResponseEntity<SubjectDto> restoreSubject(@PathVariable Long subjectId) {

        var restoredSubject = subjectService.restore(subjectId);
        return ok(toSubjectDto(restoredSubject));

    }

    @PatchMapping("/{subjectId}/addModule/{moduleId}")
    public ResponseEntity<Subject> addModuleToSubject(@PathVariable Long subjectId, @PathVariable Long moduleId) {
        return ResponseEntity.ok(subjectService.addModuleToSubject(subjectId, moduleId));
    }


}
