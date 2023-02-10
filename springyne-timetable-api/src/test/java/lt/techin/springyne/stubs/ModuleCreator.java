package lt.techin.springyne.stubs;


import lt.techin.springyne.model.Module;

public class ModuleCreator {
    public static  Module createModule(Long id) {
        Module module = new Module(id,"T1", "Test name1", false, null);

        return  module;
}
}
