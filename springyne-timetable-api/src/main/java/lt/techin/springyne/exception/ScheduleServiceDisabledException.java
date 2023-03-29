package lt.techin.springyne.exception;


public class ScheduleServiceDisabledException extends RuntimeException {


    public ScheduleServiceDisabledException() {
    }

    public ScheduleServiceDisabledException(String message) {
        super(message);
    }

}
