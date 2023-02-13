package lt.techin.springyne.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDto {

    private String number;

    private String name;

    private boolean deleted;

    public ModuleDto(String number, String name) {
        this.number = number;
        this.name = name;
    }
}
