package lt.techin.springyne.exception;

public class RoomValidationEception extends RuntimeException {

    private String message;
    private String field;
    private String error;

    private String rejectedValue;

    public RoomValidationEception() {
    }

    public RoomValidationEception(String message, String field, String error, String rejectedValue) {
        this.message = message;
        this.field = field;
        this.error = error;
        this.rejectedValue = rejectedValue;
    }

    @Override
    public String getMessage() {
        return message;
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

