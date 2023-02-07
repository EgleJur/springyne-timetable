package lt.techin.springyne.service;

import lt.techin.springyne.model.Module;
import lt.techin.springyne.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    @Autowired
    ModuleRepository moduleRepository;


    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }


    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public Module addModule(Module module) {
        return moduleRepository.save(module);
    }
}
