package lt.techin.springyne.controller;

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

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping
    public List<ModuleDto> getAllModules() {
        return moduleService.getAllModules().stream().map((ModuleMapper::toModuleDto))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ModuleDto> addModule(@Valid @RequestBody ModuleDto moduleDto) {

        Module newModule = moduleService.addModule(ModuleMapper.toModule(moduleDto));
        return ResponseEntity.ok(ModuleMapper.toModuleDto(newModule));
    }

}
