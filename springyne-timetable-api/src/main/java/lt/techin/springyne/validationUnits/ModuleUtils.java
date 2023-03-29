package lt.techin.springyne.validationUnits;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.module.Module;
import lt.techin.springyne.module.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class ModuleUtils {

    @Autowired
    private final ModuleRepository moduleRepository;

    public ModuleUtils(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public Module getModuleById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Module does not exist", "id",
                        "Module not found", String.valueOf(id)));
    }

}
