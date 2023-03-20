package lt.techin.springyne.holiday;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayDto {

    private String name;

    private LocalDate starts;

    private LocalDate ends;

    private boolean repeats;

}
