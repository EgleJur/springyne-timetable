package lt.techin.springyne.schedule;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleDto {

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

}
