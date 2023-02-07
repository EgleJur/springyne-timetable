package lt.techin.springyne.controller;

import lt.techin.springyne.dto.SubjectDto;
import lt.techin.springyne.dto.mapper.SubjectMapper;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<SubjectDto> getSubjects() {
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



}
