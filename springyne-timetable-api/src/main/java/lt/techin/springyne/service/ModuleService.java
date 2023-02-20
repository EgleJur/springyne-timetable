package lt.techin.springyne.service;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.model.Module;
import lt.techin.springyne.subject.Subject;
import lt.techin.springyne.repository.ModuleRepository;
import lt.techin.springyne.subject.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    SubjectRepository subjectRepository;

    private static final ExampleMatcher SEARCH_CONTAINS_NAME = ExampleMatcher.matchingAny()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withIgnorePaths("id", "number","deleted","modifiedDate");


    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }


    public List<Module> getAllModules() {
        return moduleRepository.findAllByOrderByDeletedAscIdAsc();
    }

    public Module addModule(Module module) {
        if (existsByNumber(module.getNumber())) {
            throw new ScheduleValidationException("Module number must be unique", "number", "Number already exists", module.getNumber());
        }
        return moduleRepository.save(module);
    }

    public boolean existsByNumber(String number) {
        return moduleRepository.existsByNumberIgnoreCase(number);
    }

    public Page<Module> searchByName(String name, int page, int pageSize) {

        Module module = new Module();
        if(name != null) {
            module.setName(name);
        }
        Example<Module> moduleExample = Example.of(module, SEARCH_CONTAINS_NAME);
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by("deleted").and(Sort.by("id")));
//                .and(Sort.by("number"))));
        return moduleRepository.findAll(moduleExample,pageable);
    }

    public Optional<Module> getModuleById(Long moduleId) {
        return moduleRepository.findById(moduleId);
    }

    public Module deleteModule(long moduleId) {
        Module moduleToDelete = moduleRepository.findById(moduleId).orElseThrow(
                () -> new ScheduleValidationException("Module does not exist", "id", "Module not found", String.valueOf(moduleId)));
        if (!moduleToDelete.isDeleted()) {
            moduleToDelete.setDeleted(true);
            return moduleRepository.save(moduleToDelete);
        } else {
            return moduleToDelete;
        }
    }

    public Module restoreModule(long moduleId) {
        Module moduleToRestore = moduleRepository.findById(moduleId).orElseThrow(
                () -> new ScheduleValidationException("Module does not exist", "id", "Module not found", String.valueOf(moduleId)));
        if (moduleToRestore.isDeleted()) {
            moduleToRestore.setDeleted(false);
            return moduleRepository.save(moduleToRestore);
        } else {
            return moduleToRestore;
        }
    }

    public Module updateModule(long moduleId, Module module) {
        Module updatedModule = moduleRepository.findById(moduleId).orElseThrow(
                () -> new ScheduleValidationException("Module does not exist", "id", "Module not found", String.valueOf(moduleId)));
        if (!updatedModule.getNumber().equals(module.getNumber())) {
            if(updatedModule.getNumber().equalsIgnoreCase(module.getNumber())) {
                updatedModule.setNumber(module.getNumber());
            } else if (moduleRepository.existsByNumberIgnoreCase(module.getNumber())) {
                throw new ScheduleValidationException("Module number must be unique", "number", "Number already exists", module.getNumber());
            } else {
                updatedModule.setNumber(module.getNumber());
            }
        }
        if (!updatedModule.getName().equals(module.getName())) {
            if (module.getName().equals("") || module.getName() == null) {
                throw new ScheduleValidationException("Module name cannot be empty", "name", "Name is empty", module.getName());
            } else {
                updatedModule.setName(module.getName());
            }
        }
        return moduleRepository.save(updatedModule);
    }

    public List<Subject> getAllSubjectsByModule(Long moduleId) {
        if (moduleRepository.existsById(moduleId)) {
            return subjectRepository.findByModuleIdOrderByDeletedAscIdAsc(moduleId);
        } else {
            throw new ScheduleValidationException("Module does not exist", "id", "Module not found", String.valueOf(moduleId));
        }
    }

    public List<Subject> getAllSubjectsAvailableByModule(Long moduleId) {
        if (moduleRepository.existsById(moduleId)) {
            return subjectRepository.findByDeletedFalseAndModuleIdNotOrModuleIdIsNull(moduleId);
        } else {
            throw new ScheduleValidationException("Module does not exist", "id", "Module not found", String.valueOf(moduleId));
        }
    }

//    public Subject addModuleToSubject(Long moduleId, Long subjectId) {
//        Module moduleToAdd = moduleRepository.findById(moduleId).orElseThrow(
//                () -> new ScheduleValidationException("Module does not exist", "id", "Module not found", String.valueOf(moduleId)));
//        Subject updatedSubject = subjectRepository.findById(subjectId).orElseThrow(
//                () -> new ScheduleValidationException("Subject does not exist", "id", "Subject not found", String.valueOf(subjectId)));
//        updatedSubject.getModule().add(moduleToAdd);
//        return subjectRepository.save(updatedSubject);
//    }
//
//    public Subject removeModuleFromSubject(Long moduleId, Long subjectId) {
//        Module moduleToRemove = moduleRepository.findById(moduleId).orElseThrow(
//                () -> new ScheduleValidationException("Module does not exist", "id", "Module not found", String.valueOf(moduleId)));
//        Subject updatedSubject = subjectRepository.findById(subjectId).orElseThrow(
//                () -> new ScheduleValidationException("Subject does not exist", "id", "Subject not found", String.valueOf(subjectId)));
//        updatedSubject.getModule().remove(moduleToRemove);
//        return subjectRepository.save(updatedSubject);
//    }
}
