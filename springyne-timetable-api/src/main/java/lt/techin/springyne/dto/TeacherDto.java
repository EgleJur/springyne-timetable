package lt.techin.springyne.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {

    private String number;

    private String name;

    private String lastname;
    private String teams_mail;
    private String email;
    private String phone;
    private String hours;
    private String subject;
    private String shift;

    private boolean deleted;

    public TeacherDto(String number, String name, String lastname, String teams_mail, String email, String phone, String hours, String subject, String shift) {
        this.number = number;
        this.lastname = lastname;
        this.name = name;
        this.teams_mail =teams_mail;
        this.email=email;
        this.phone=phone;
        this.hours=hours;
        this.subject=subject;
        this.shift=shift;
    }
}
