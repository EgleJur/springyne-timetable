package lt.techin.springyne.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.dto.ModuleDto;
import lt.techin.springyne.dto.mapper.ModuleMapper;
import lt.techin.springyne.model.Module;
import lt.techin.springyne.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<Object> addModule(@Valid @RequestBody ModuleDto moduleDto) {
        if (moduleService.existsByNumber(moduleDto.getNumber())) {
            return ResponseEntity.badRequest().body("Toks numeris jau egzistuoja");
        }
        Module newModule = moduleService.addModule(ModuleMapper.toModule(moduleDto));
        return ResponseEntity.ok(ModuleMapper.toModuleDto(newModule));
    }

    @GetMapping("/search")
    public List<Module> filterModulesByNamePaged(@RequestParam(required = false) String name,
                                                    @RequestParam int page, @RequestParam int pageSize) {
        return moduleService.searchByName(name,page,pageSize).stream()
                .collect(Collectors.toList());
    }

}
