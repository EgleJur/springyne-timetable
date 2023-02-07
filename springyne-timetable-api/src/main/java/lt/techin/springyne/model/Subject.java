package lt.techin.springyne.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "SUBJECT_TABLE")
//@SQLDelete(sql = "UPDATE Subject SET deleted = true WHERE id=?")
//@Where(clause = "deleted=false")
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

//    active Boolean default true
//
//    select * from Customers where active = true;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "room_id")
//
//    private Room room;


    public Subject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUpdatedDate() {
        return lastUpdated;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.lastUpdated = updatedDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
