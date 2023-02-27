package lt.techin.springyne.program;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramDto {

    private String name;

    private String description;

    private boolean deleted;
}
