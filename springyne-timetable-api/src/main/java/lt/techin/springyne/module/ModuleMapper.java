package lt.techin.springyne.module;

public class ModuleMapper {

    public static Module toModule(ModuleDto moduleDto) {

        Module module = new Module();
        module.setNumber(moduleDto.getNumber());
        module.setName(moduleDto.getName());
        module.setDeleted(moduleDto.isDeleted());

        return module;
    }

    public static ModuleDto toModuleDto(Module module) {

        ModuleDto moduleDto = new ModuleDto();
        moduleDto.setNumber(module.getNumber());
        moduleDto.setName(module.getName());
        moduleDto.setDeleted(module.isDeleted());

        return moduleDto;
    }
}
