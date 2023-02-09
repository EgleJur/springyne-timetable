package lt.techin.springyne.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

//    @NotBlank(message = "Butina ivesti kabineto pavadinima")
    private String name;

//    @NotBlank (message = "Butina ivesti pastato adresa")
    private String building;

    private String description;

    private boolean deleted;

    public RoomDto(String name, String building, String description) {
        this.name = name;
        this.building = building;
        this.description = description;
    }
}
