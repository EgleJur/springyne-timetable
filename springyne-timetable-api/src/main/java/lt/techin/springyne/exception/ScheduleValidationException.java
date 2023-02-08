package lt.techin.springyne.exception;

//Galima naudoti anotacijas tiesiog Exception lygmeni, jeigu nenorime tureti centralizuotos logikos
//@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Actor Not Found")
public class ScheduleValidationException extends RuntimeException {

    private String field;
    private String error;

    private String rejectedValue;

    public ScheduleValidationException() {
    }

    public ScheduleValidationException(String message, String field, String error, String rejectedValue) {
        super(message);
        this.field = field;
        this.error = error;
        this.rejectedValue = rejectedValue;
    }

    public String getField() {
        return field;
    }

    public String getError() {
        return error;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }
}
