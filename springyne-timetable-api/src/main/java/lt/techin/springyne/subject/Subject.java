package lt.techin.springyne.subject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lt.techin.springyne.module.Module;
import lt.techin.springyne.room.Room;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SUBJECT_TABLE")
@Data
@AllArgsConstructor
@EqualsAndHashCode

public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime last_Updated;

    private boolean deleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    private Module module;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "subjects_in_rooms",
            joinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id"))
    private Set<Room> rooms;

    public Subject() {
        this.rooms = new HashSet<>();
    }

    @PrePersist
    private void prePersist() {
        last_Updated = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        last_Updated = LocalDateTime.now();
    }

}