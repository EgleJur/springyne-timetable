package lt.techin.springyne.controller;

import lt.techin.springyne.dto.SubjectDto;
import lt.techin.springyne.dto.mapper.SubjectMapper;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static lt.techin.springyne.dto.mapper.SubjectMapper.toSubject;
import static lt.techin.springyne.dto.mapper.SubjectMapper.toSubjectDto;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("/api/v1/subject")
@Validated
public class SubjectControler {

    @Autowired
    SubjectService subjectService;

    public SubjectControler(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    @ResponseBody
    public List<SubjectDto> getAllSubjects() {
        return subjectService.getAll().stream().map(SubjectMapper::toSubjectDto)
                .collect(Collectors.toList());

    }
    @GetMapping("/{subjectId}")
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



}
