package lt.techin.springyne.dto;

import lt.techin.springyne.model.ModuleInfo;
import lt.techin.springyne.model.Room;

public class SubjectDto {

    private String name;
    private String description;



    public SubjectDto() {
    }

    public SubjectDto(String name, String description, ModuleInfo module, Room room) {

        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
