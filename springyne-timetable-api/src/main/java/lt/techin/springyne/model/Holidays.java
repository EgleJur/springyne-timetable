package lt.techin.springyne.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Holidays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @NotNull
    private String name;

    //    @NotNull
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private LocalDate starts;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date starts;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date ends;

//    @NotNull
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private LocalDate ends;

    private boolean repeats = Boolean.FALSE;

}
