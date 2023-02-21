package lt.techin.springyne.controller;

import lt.techin.springyne.dto.ProgramDto;
import lt.techin.springyne.dto.mapper.ProgramMapper;
import lt.techin.springyne.model.Program;
import lt.techin.springyne.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Optional<Program> getModuleById(@PathVariable Long programId) {
        return programService.getProgramById(programId);
    }

    @GetMapping("/search")
    public Page<Program> filterProgramsByNamePaged(@RequestParam(required = false) String name,
                                                 @RequestParam int page, @RequestParam int pageSize) {
        return programService.searchByName(name,page,pageSize);
    }
}
