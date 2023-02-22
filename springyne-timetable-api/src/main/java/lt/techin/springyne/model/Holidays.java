package lt.techin.springyne.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private int yearStarts;

    @NotNull
    private int yearEnds;

    @Min(value=1)
    @Max(value=12)
    @NotNull
    private int monthStarts;
    @Min(value=1)
    @Max(value=12)
    @NotNull
    private int monthEnds;
    @Min(value=1)
    @Max(value=31)
    @NotNull
    private int dayStarts;
    @Min(value=1)
    @Max(value=31)
    @NotNull
    private int dayEnds;

    private boolean deleted = Boolean.FALSE;
    private boolean repeats = Boolean.FALSE;

    @Override
    public String toString() {
        return "Holidays{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", yearStarts=" + yearStarts + " monthStarts=" + monthStarts + " dayStarts=" + dayStarts +
                " yearEnds=" + yearEnds +" monthEnds=" + monthEnds +" dayEnds=" + dayEnds +
                ", deleted=" + deleted +
                ", repeats=" + repeats +
                '}';
    }
}
