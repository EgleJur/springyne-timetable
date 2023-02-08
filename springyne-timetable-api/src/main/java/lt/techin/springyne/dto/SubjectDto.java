package lt.techin.springyne.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lt.techin.springyne.model.Module;
import lt.techin.springyne.model.Room;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {

    private Long id;
    private String name;
    private String description;
    private Set<Module> module;
    private Set<Room> room;

}
