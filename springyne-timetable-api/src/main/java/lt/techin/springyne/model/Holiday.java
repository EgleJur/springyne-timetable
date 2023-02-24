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
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @NotNull
    private String name;


    @NotNull
    @Temporal(TemporalType.DATE)
    private Date starts;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date ends;

    private boolean repeats;

}
