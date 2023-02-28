package lt.techin.springyne.program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/programs")
public class ProgramController {

    @Autowired
    ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @GetMapping
    public List<Program> getAllPrograms() {
        return programService.getAllModules();
    }

    @PostMapping
    public ResponseEntity<Program> createProgram(@RequestBody ProgramDto programDto,
                                                 @RequestParam Long subjectId,
                                                 @RequestParam Integer hours) {
        return ResponseEntity.ok(programService.createProgram(ProgramMapper.toProgram(programDto),
                subjectId, hours));
    }

    @GetMapping("/{programId}")
    public Optional<Program> getProgramById(@PathVariable Long programId) {
        return programService.getProgramById(programId);
    }

    @GetMapping("/search")
    public Page<Program> filterProgramsByNamePaged(@RequestParam(required = false) String name,
                                                 @RequestParam int page, @RequestParam int pageSize) {
        return programService.searchByName(name,page,pageSize);
    }

    @PatchMapping("/delete/{programId}")
    public ResponseEntity<Program> deleteProgram(@PathVariable Long programId) {
        return ResponseEntity.ok(programService.deleteProgram(programId));
    }

    @PatchMapping("/restore/{programId}")
    public ResponseEntity<Program> restoreProgram(@PathVariable Long programId) {
        return ResponseEntity.ok(programService.restoreProgram(programId));
    }

    @PatchMapping("/update/{programId}")
    public ResponseEntity<Program> updateProgram(@PathVariable Long programId, @Valid @RequestBody ProgramDto programDto,
                                                 @RequestParam(required = false) Long subjectId,
                                                 @RequestParam(required = false) Integer hours) {
        return ResponseEntity.ok(programService.updateProgram(programId, ProgramMapper.toProgram(programDto), subjectId, hours));
    }
    @PatchMapping("/{programId}/removeSubject/{subjectId}")
    public ResponseEntity<Program> removeSubjectFromProgram(@PathVariable Long programId, @PathVariable Long subjectId) {
        return ResponseEntity.ok(programService.removeSubjectFromProgram(programId,subjectId));
    }

}
