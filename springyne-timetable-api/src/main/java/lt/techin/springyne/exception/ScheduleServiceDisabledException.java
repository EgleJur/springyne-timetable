package lt.techin.springyne.exception;

//Galima naudoti anotacijas tiesiog Exception lygmeni, jeigu nenorime tureti centralizuotos logikos
//@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Actor Not Found")
public class ScheduleServiceDisabledException extends RuntimeException {


    public ScheduleServiceDisabledException() {
    }

    public ScheduleServiceDisabledException(String message) {
        super(message);
    }

}
