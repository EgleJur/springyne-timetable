package lt.techin.springyne.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {

    private String number;

    private String name;

    private String lastname;

    private boolean deleted;

    public TeacherDto(String number, String name, String lastname) {
        this.number = number;
        this.lastname = lastname;
        this.name = name;
    }
}
