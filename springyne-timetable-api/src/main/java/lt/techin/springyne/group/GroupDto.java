package lt.techin.springyne.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {

    //    private Long id;
    private String name;
    private String groupYear;
    private int students;


    public GroupDto(String name) {
        this.name = name;
    }
}