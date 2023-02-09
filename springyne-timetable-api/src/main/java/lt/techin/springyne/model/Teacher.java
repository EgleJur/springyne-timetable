package lt.techin.springyne.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;
    private String teams_email;
    private String email;
    private String phone_number;
    private String hours;
    private String subject;
    private String shift;

    public Teacher() {}

    public Teacher(String name,String lastname, String teams_email, String email, String phone_number, String hours, String subject, String shift
    ) {
        this.name = name;
        this.lastname = lastname;
        this.teams_email = teams_email;
        this.email = email;
        this.phone_number = phone_number;
        this.hours = hours;
        this.subject = subject;
        this.shift = shift;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTeams_email() {
        return teams_email;
    }

    public void setTeams_email(String teams_email) {
        this.teams_email = teams_email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }
}
