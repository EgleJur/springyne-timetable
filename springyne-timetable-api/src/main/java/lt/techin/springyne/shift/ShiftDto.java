package lt.techin.springyne.shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDto {
    private String name;
    private int starts;
    private int ends;
    private int visible;
}
