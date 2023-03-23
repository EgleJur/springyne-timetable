package lt.techin.springyne.exception;

import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;


public class ExceptionTest {

    private final ApiExceptionHandler apiExceptionHandler = new ApiExceptionHandler();
    @Test
    void handleValidationExceptions() {
        apiExceptionHandler.handleValidationExceptions(null);
    }

    @Test
    public void testToErrorFieldDto_withFieldError() {

        String name = "fieldName";
        String rejectedValue = "rejectedValue";
        String errorMessage = "error message";
        FieldError fieldError = new FieldError("objectName", name, rejectedValue, false, null, null, errorMessage);

        ErrorFieldDto result = ErrorFieldMapper.toErrorFieldDto(fieldError);

        assertEquals(name, result.getName());
        assertEquals(rejectedValue, result.getRejectedValue());
        assertEquals(errorMessage, result.getError());
    }

    private void assertEquals(String name, String name1) {
    }

    @Test
    public void testToErrorFieldDto_withObjectError() {

        String errorMessage = "error message";
        ObjectError objectError = new ObjectError("objectName", errorMessage);

        ErrorFieldDto result = ErrorFieldMapper.toErrorFieldDto(objectError);

        assertEquals(null, result.getName());
        assertEquals(null, result.getRejectedValue());
        assertEquals(errorMessage, result.getError());
    }

    @Test
    public void testConstructorWithNoArgs() {
        ScheduleServiceDisabledException ex = new ScheduleServiceDisabledException();

        assertEquals(null, ex.getMessage());
    }

    @Test
    public void testConstructorWithMessage() {
        String message = "schedule service is disabled";
        ScheduleServiceDisabledException ex = new ScheduleServiceDisabledException(message);

        assertEquals(message, ex.getMessage());
    }
}



