package lt.techin.springyne.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {

    private String name;
    private String teamsEmail;
    private String email;
    private String phone;
    private Integer hours;

    private boolean deleted;

}
