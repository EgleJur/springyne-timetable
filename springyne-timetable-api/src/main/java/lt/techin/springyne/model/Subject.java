package lt.techin.springyne.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
//@Builder
@Table(name = "SUBJECT_TABLE")
//@SQLDelete(sql = "UPDATE Subject SET deleted = true WHERE id=?")
//@Where(clause = "deleted=false")
@Data
@AllArgsConstructor
//@NoArgsConstructor
@EqualsAndHashCode

public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private Long id;

    @NotBlank
//    @Column(name = "name")
    private String name;

//    @Column(name = "description")
    private String description;

    @LastModifiedDate
//    @Column(name = "LAST_UPDATED")
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

//    public void addModule(Module module){
//    this.modules.add(module);
//    module.getSubj().add(this);
//
//}
//    public void addRoom(Room room){
//        this.rooms.add(room);
//    }


    @PrePersist
    private void prePersist() {
        last_Updated = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        last_Updated = LocalDateTime.now();
    }

}
