package lt.techin.springyne.service;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.model.Shift;
import lt.techin.springyne.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



@Service
public class ShiftService {
    @Autowired
    ShiftRepository shiftRepository;

    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }
    public List<Shift> getShifts() {
        return shiftRepository.findAll();
    }
    public Shift createShift(Shift shift) {
        if(shiftRepository.existsByNameIgnoreCase(shift.getName())) {
            throw new ScheduleValidationException("Shift name must be unique", "name", "Name already exists", shift.getName());
        } else if (shift.getStarts() > shift.getEnds()) {
            throw new ScheduleValidationException("Shift starts must be less than or equal to ends", "starts", "Starts ir greater than ends", Integer.toString(shift.getStarts()));
        }
        shift.setLastUpdated(LocalDateTime.now());
        shift.setVisible(1);
        return shiftRepository.save(shift);
    }
    public Shift editShift(Long shiftId, Shift shift) {
        var tempShift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ScheduleValidationException("Shift id error", "id", "Shift not found", Long.toString(shift.getId())));
        var testFindName = shiftRepository.findOneByNameIgnoreCase(shift.getName());
        if (testFindName != null){
            if(testFindName.getId() != shiftId) {
                throw new ScheduleValidationException("Shift name already taken", "name", "Name already exists", shift.getName());
            }
        }
        if (shift.getStarts() > shift.getEnds()) {
            throw new ScheduleValidationException("Shift starts must be less than or equal to ends", "starts", "Starts ir greater than ends", Integer.toString(shift.getStarts()));
        }
        tempShift.setName(shift.getName());
        tempShift.setStarts(shift.getStarts());
        tempShift.setEnds(shift.getEnds());
        tempShift.setLastUpdated(LocalDateTime.now());
        tempShift.setVisible(shift.getVisible());
        return shiftRepository.save(tempShift);
    }
    public Optional<Shift> getShift(Long shiftId){
        return shiftRepository.findById(shiftId);
    }
}
