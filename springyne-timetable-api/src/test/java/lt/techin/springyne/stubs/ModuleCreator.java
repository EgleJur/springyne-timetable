package lt.techin.springyne.stubs;


import lt.techin.springyne.module.Module;

public class ModuleCreator {
    public static  Module createModule(Long id) {
        Module module = new Module(id,"001", "Informacinių sistemų projektavimas", false, null);

        return  module;
}
}
