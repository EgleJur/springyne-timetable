package lt.techin.springyne.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SUBJECT_TABLE")
//@SQLDelete(sql = "UPDATE Subject SET deleted = true WHERE id=?")
//@Where(clause = "deleted=false")
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @CreatedDate
    private LocalDateTime lastUpdated;

    private boolean deleted = Boolean.FALSE;


//    @ManyToMany
//    private Module module;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "subject_and_modules",
            joinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "module_id", referencedColumnName = "id"))
    Set<ModuleInfo> moduleInfos;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "subjects_in_rooms", joinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id"))
    private Set<Room> rooms;



    public Subject() {
        moduleInfos = new HashSet<>();
        rooms = new HashSet<>();

    }

    @PrePersist
    private void prePersist() {
        lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        lastUpdated = LocalDateTime.now();
    }


}
