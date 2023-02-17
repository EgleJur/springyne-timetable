package lt.techin.springyne.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

    private String name;

    private String building;

    private String description;

//    private boolean deleted;


    public RoomDto(String name, String building) {
        this.name = name;
        this.building = building;
    }

}
