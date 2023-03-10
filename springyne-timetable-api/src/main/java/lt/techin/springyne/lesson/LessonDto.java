package lt.techin.springyne.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {

    private LocalDate lessonDate;

    private Integer lessonTime;

}