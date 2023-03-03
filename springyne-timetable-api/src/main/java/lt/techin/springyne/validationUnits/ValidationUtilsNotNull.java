package lt.techin.springyne.validationUnits;

import lt.techin.springyne.exception.ScheduleValidationException;

public class ValidationUtilsNotNull {

    public static void isValidById(Long id) {
        if (id == null || id.equals("")) {
            throw new ScheduleValidationException("Id cannot be empty", "id",
                    "Id is empty", String.valueOf(id));
        }
    }
    public static void isValidByName(String name) {
        if (name == null || name.isEmpty() || name.equals("")) {
            throw new ScheduleValidationException("Name cannot be empty", "name",
                    "Name is empty", name);
        }
    }

    public static void isValidByGroupYear(String year){
        if (year == null || year.isEmpty() || year.equals("")) {
            throw new ScheduleValidationException("Group year cannot be empty", "year",
                    "Year is empty", year);
        }
    }

}

