package lt.techin.springyne.service;

import lt.techin.springyne.model.Module;
import lt.techin.springyne.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    @Autowired
    ModuleRepository moduleRepository;

    private static final ExampleMatcher SEARCH_CONTAINS_NAME = ExampleMatcher.matchingAny()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withIgnorePaths("id", "number","deleted","modifiedDate");


    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }


    public List<Module> getAllModules() {
        return moduleRepository.findAllByOrderByDeletedAscNameAscNumberAsc();
    }

    public Module addModule(Module module) {
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
}
