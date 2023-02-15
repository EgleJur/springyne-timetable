package lt.techin.springyne.dto.mapper;

import lt.techin.springyne.dto.ShiftDto;
import lt.techin.springyne.model.Shift;

public class ShiftMapper {
    public static Shift toShift(ShiftDto shiftDto){
        Shift shift = new Shift();
        shift.setName(shiftDto.getName());
        shift.setStarts(shiftDto.getStarts());
        shift.setEnds(shiftDto.getEnds());
        shift.setVisible(shiftDto.getVisible());
        return shift;
    }
    public static ShiftDto toShiftDto(Shift shift){
        ShiftDto shiftDto = new ShiftDto();
        shiftDto.setName(shift.getName());
        shiftDto.setStarts(shift.getStarts());
        shiftDto.setEnds(shift.getEnds());
        shiftDto.setVisible(shift.getVisible());
        return shiftDto;
    }
}
