package lt.techin.springyne.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

    private String name;

    private String building;

    private String description;

    private boolean deleted;

    private LocalDateTime lastModifiedDate;

    public RoomDto(String name, String building, String description) {
        this.name = name;
        this.building = building;
        this.description = description;
    }

//    public RoomDto(String name, String building, String description, boolean deleted) {
//        this.name = name;
//        this.building = building;
//        this.description = description;
//        this.deleted = deleted;
//    }
}
