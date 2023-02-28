package lt.techin.springyne.holiday;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayDto {

    private String name;

    private String starts;

    private String ends;

    private boolean repeats;

}