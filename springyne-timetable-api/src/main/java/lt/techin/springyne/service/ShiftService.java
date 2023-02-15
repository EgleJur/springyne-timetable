package lt.techin.springyne.service;

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
            throw new IllegalArgumentException();
        }
        shift.setLastUpdated(LocalDateTime.now());
        shift.setVisible(1);
        return shiftRepository.save(shift);
    }
    public Shift editShift(Long shiftId, Shift shift) {
        var tempShift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ValidationException());
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
