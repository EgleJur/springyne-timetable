package lt.techin.springyne.validationUnits;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.shift.Shift;
import lt.techin.springyne.shift.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class ShiftUtils {

    @Autowired
    ShiftRepository shiftRepository;

    public ShiftUtils(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    public Shift getShiftById(Long id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Shift does not exist", "id",
                        "Shift not found", String.valueOf(id)));
    }

}
