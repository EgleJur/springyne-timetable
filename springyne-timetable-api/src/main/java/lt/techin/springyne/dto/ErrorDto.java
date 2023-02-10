package lt.techin.springyne.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
@Data
@AllArgsConstructor
public class ErrorDto {
    private String url;
    private List<ErrorFieldDto> fields;
    private String message;
    private Integer status;
    private String error;
    private String path;

    private LocalDateTime timestamp;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorDto errorDto = (ErrorDto) o;
        return Objects.equals(url, errorDto.url) && Objects.equals(fields, errorDto.fields) && Objects.equals(message, errorDto.message) && Objects.equals(status, errorDto.status) && Objects.equals(error, errorDto.error) && Objects.equals(path, errorDto.path) && Objects.equals(timestamp, errorDto.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, fields, message, status, error, path, timestamp);
    }

    @Override
    public String toString() {
        return "ErrorDto{" +
                "url='" + url + '\'' +
                ", fields=" + fields +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", path='" + path + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
