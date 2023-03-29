package lt.techin.springyne.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonBlock {

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer startTime;

    private Integer endTime;

}
