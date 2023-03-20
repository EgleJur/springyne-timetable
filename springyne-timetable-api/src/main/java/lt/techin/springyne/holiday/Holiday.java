package lt.techin.springyne.holiday;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @NotNull
    private String name;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate starts;


    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ends;


    private boolean repeats;

}
