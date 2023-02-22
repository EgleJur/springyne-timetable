package lt.techin.springyne.service;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.model.Holidays;
import lt.techin.springyne.repository.HolidaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HolidayService {
    @Autowired
    HolidaysRepository holidaysRepository;

    private Holidays getHolidayById(Long id) {
        return holidaysRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Holiday does not exist", "id",
                        "Holiday not found", String.valueOf(id)));
    }

    public HolidayService(HolidaysRepository holidaysRepository) {
        this.holidaysRepository = holidaysRepository;
    }

    public List<Holidays> getAllHolidays(){
        return holidaysRepository.findAll();
    }

    public Holidays delete(Long holidayId) {

        Holidays existingHoliday = getHolidayById(holidayId);
        if (!existingHoliday.isDeleted()) {
            existingHoliday.setDeleted(true);
            return holidaysRepository.save(existingHoliday);
        } else {
            return existingHoliday;
        }

    }
    //
//
    public Holidays restore(Long id) {
        var existingHoliday = getHolidayById(id);

        if (existingHoliday.isDeleted()) {
            existingHoliday.setDeleted(false);
            return holidaysRepository.save(existingHoliday);
        } else {
            return existingHoliday;
        }
    }
}
