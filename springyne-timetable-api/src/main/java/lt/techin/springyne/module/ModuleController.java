package lt.techin.springyne.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/modules")
public class ModuleController {

    @Autowired
    ModuleService moduleService;

    @Autowired
    ObjectMapper objectMapper;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping
    public List<Module> getAllModules() {
        return moduleService.getAllModules();
    }

    @PostMapping
    public ResponseEntity<Module> addModule(@Valid @RequestBody ModuleDto moduleDto) {
        return ResponseEntity.ok(moduleService.addModule(ModuleMapper.toModule(moduleDto)));
    }

    @GetMapping("/search")
    public Page<Module> filterModulesByNamePaged(@RequestParam(required = false) String name,
                                               @RequestParam int page, @RequestParam int pageSize) {
        return moduleService.searchByName(name,page,pageSize);
    }

    @GetMapping("/{moduleId}")
    public Optional<Module> getModuleById(@PathVariable Long moduleId) {
        return moduleService.getModuleById(moduleId);
    }

    @PatchMapping("/delete/{moduleId}")
    public ResponseEntity<Module> deleteModule(@PathVariable Long moduleId) {
        return ResponseEntity.ok(moduleService.deleteModule(moduleId));
    }

    @PatchMapping("/restore/{moduleId}")
    public ResponseEntity<Module> restoreModule(@PathVariable Long moduleId) {
        return ResponseEntity.ok(moduleService.restoreModule(moduleId));
    }

    @PatchMapping("/update/{moduleId}")
    public ResponseEntity<Module> updateModule(@PathVariable Long moduleId, @Valid @RequestBody ModuleDto moduleDto) {
        return ResponseEntity.ok(moduleService.updateModule(moduleId, ModuleMapper.toModule(moduleDto)));
    }

    @GetMapping("/subjects/{moduleId}")
    public List<Subject> getAllSubjectsByModule(@PathVariable Long moduleId) {
        return moduleService.getAllSubjectsByModule(moduleId);
    }

    @GetMapping("/subjects/available/{moduleId}")
    public List<Subject> getAllSubjectsAvailableByModule(@PathVariable Long moduleId) {
        return moduleService.getAllSubjectsAvailableByModule(moduleId);
    }
}
