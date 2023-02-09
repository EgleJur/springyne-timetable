package lt.techin.springyne.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Table(name = "e_room_TABLE")
public class Room {
    @Id

    private Long id;
//    private String name;

    public Room() {
    }
}
