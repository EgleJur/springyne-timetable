package lt.techin.springyne.controller;

import lt.techin.springyne.dto.SubjectDto;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static lt.techin.springyne.dto.mapper.SubjectMapper.toSubject;
import static lt.techin.springyne.dto.mapper.SubjectMapper.toSubjectDto;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/subjects")

public class SubjectControler {

    @Autowired
    SubjectService subjectService;

    public SubjectControler(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/search")
    public Page<Subject> searchByNamePaged(@RequestParam(required = false) String name,
                                           @RequestParam int page,
                                           @RequestParam int pageSize) {
        return subjectService.searchByNamePaged(name, page, pageSize);
    }

    @GetMapping("/byModule/search")

    public Page<Subject> getByModule(@RequestParam String name, @RequestParam int page,
                                           @RequestParam int pageSize){
        return subjectService.getByModule(name, page, pageSize);
    }
    @GetMapping("/view/{subjectId}")
    public ResponseEntity<Subject> getSubject(@PathVariable Long subjectId) {
        var subjectOptional = subjectService.getById(subjectId);

        var responseEntity = subjectOptional
                .map(subject -> ok(subject))
                .orElseGet(() -> ResponseEntity.notFound().build());

        return responseEntity;
    }

    @PostMapping
     public ResponseEntity<SubjectDto> createSubject(@RequestBody SubjectDto subjectDto) {
        var createdSubject = subjectService.create(toSubject(subjectDto));

        return ok(toSubjectDto(createdSubject));
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


}
