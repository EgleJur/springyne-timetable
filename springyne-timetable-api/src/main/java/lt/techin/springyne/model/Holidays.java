package lt.techin.springyne.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

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

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date starts;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ends;

    private boolean deleted = Boolean.FALSE;
    private boolean repeats = Boolean.FALSE;

}
