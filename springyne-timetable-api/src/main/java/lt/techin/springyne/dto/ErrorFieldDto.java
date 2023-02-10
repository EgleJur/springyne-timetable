package lt.techin.springyne.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorFieldDto {

    private String name;
    private String error;

    private String rejectedValue;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorFieldDto that = (ErrorFieldDto) o;
        return Objects.equals(name, that.name) && Objects.equals(error, that.error) && Objects.equals(rejectedValue, that.rejectedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, error, rejectedValue);
    }

    @Override
    public String toString() {
        return "ErrorFieldDto{" +
                "name='" + name + '\'' +
                ", error='" + error + '\'' +
                ", rejectedValue='" + rejectedValue + '\'' +
                '}';
    }

}
