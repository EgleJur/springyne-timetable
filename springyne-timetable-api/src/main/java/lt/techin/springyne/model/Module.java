package lt.techin.springyne.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name="MODULE_TABLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String number;

    @NotBlank
    private String name;

    private boolean deleted = Boolean.FALSE;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @PrePersist
    private void prePersist() {
        modifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        modifiedDate = LocalDateTime.now();
    }

}
