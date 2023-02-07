package lt.techin.springyne.controller;

import lt.techin.springyne.dto.SubjectDto;
import lt.techin.springyne.dto.mapper.SubjectMapper;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

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


//    @RestController
//    @RequestMapping("api/v1/modules")
//    public class ModuleController {
//
//        @Autowired
//        ModuleService moduleService;
//
//        public ModuleController(ModuleService moduleService) {
//            this.moduleService = moduleService;
//        }
//
//        @GetMapping
//        public List<ModuleDto> getAllModules() {
//            return moduleService.getAllModules().stream().map((ModuleMapper::toModuleDto))
//                    .collect(Collectors.toList());
//        }
//
//        @PostMapping
//        public ResponseEntity<ModuleDto> addModule(@Valid @RequestBody ModuleDto moduleDto) {
//
//            Module newModule = moduleService.addModule(ModuleMapper.toModule(moduleDto));
//            return ResponseEntity.ok(ModuleMapper.toModuleDto(newModule));
//        }
//
//    }


}
