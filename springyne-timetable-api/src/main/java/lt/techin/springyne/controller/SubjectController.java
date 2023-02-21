package lt.techin.springyne.controller;

import lt.techin.springyne.model.Subject;
import lt.techin.springyne.service.SubjectService;
import lt.techin.springyne.dto.SubjectDto;
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
                                           @RequestParam(required = false) String moduleName,
                                           @RequestParam int page,
                                           @RequestParam int pageSize) {
        return subjectService.searchByNamePaged(name, moduleName, page, pageSize);
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
    public ResponseEntity<SubjectDto> createSubjectDto(@RequestBody SubjectDto subjectDto) {
        var createdSubject = subjectService.createSubjectDto(toSubject(subjectDto));

        return ok(toSubjectDto(createdSubject));
    }
    @PostMapping(value = "/createSubject")
    public ResponseEntity<SubjectDto> createSubject(@RequestBody SubjectDto subjectDto,
                                                    @RequestParam Long moduleId,
                                                    @RequestParam(required = false) Long roomId) {
        var createdSubject = subjectService.createSubject(moduleId, roomId, toSubject(subjectDto));
        return ok(toSubjectDto(createdSubject));
    }


    @PatchMapping("/edit/{subjectId}")
    public ResponseEntity<Subject> editSubject(@PathVariable Long subjectId,
                                               @RequestBody SubjectDto subjectDto,
                                               @RequestParam(required = false) Long moduleId,
                                               @RequestParam(required = false) Long roomId) {
        var updatedSubject = subjectService.edit(subjectId, toSubject(subjectDto), moduleId, roomId);

        return ok(updatedSubject);
    }


    @PatchMapping("/delete/{subjectId}")
    public ResponseEntity<Subject> deleteSubject(@PathVariable Long subjectId) {

        var updatedSubject = subjectService.delete(subjectId);
        return ok(updatedSubject);

    }

    @PatchMapping("/restore/{subjectId}")
    public ResponseEntity<Subject> restoreSubject(@PathVariable Long subjectId) {

        var restoredSubject = subjectService.restore(subjectId);
        return ok(restoredSubject);

    }

    @PatchMapping("/{subjectId}/addModule/{moduleId}")
    public ResponseEntity<Subject> addModuleToSubject(@PathVariable Long subjectId, @PathVariable Long moduleId) {
        return ResponseEntity.ok(subjectService.addModuleToSubject(subjectId, moduleId));
    }

    @PatchMapping("/{subjectId}/deleteRoom/{roomId}")
    public void deleteRoomFromSubject(@PathVariable Long subjectId, @PathVariable Long roomId) {
        subjectService.deleteRoomFromSubject(subjectId, roomId);
    }
    @PatchMapping("/{subjectId}/addRoom/{roomId}")
    public void addRoomFromSubject(@PathVariable Long subjectId, @PathVariable Long roomId) {
        subjectService.addRoomFromSubject(subjectId, roomId);
    }


}