package lt.techin.springyne.dto.mapper;

import lt.techin.springyne.dto.ModuleDto;
import lt.techin.springyne.model.Module;

public class ModuleMapper {

    public static Module toModule(ModuleDto moduleDto) {

        Module module = new Module();
        module.setNumber(moduleDto.getNumber());
        module.setName(moduleDto.getName());
        return module;
    }

    public static ModuleDto toModuleDto(Module module) {

        ModuleDto moduleDto = new ModuleDto();
        moduleDto.setNumber(module.getNumber());
        moduleDto.setName(module.getName());

        return moduleDto;
    }
}
