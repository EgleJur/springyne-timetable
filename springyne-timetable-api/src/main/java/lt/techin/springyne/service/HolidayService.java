package lt.techin.springyne.service;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.model.Holidays;
import lt.techin.springyne.repository.HolidaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
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

    public List<Holidays> getAll(){

        return holidaysRepository.findAll();
    }
    public List<Holidays> getAllHolidays(){

        int yearNow = LocalDate.now().getYear();
        return holidaysRepository.findAllHolidays(yearNow);
    }

    public boolean delete(Long id) {

        try {
            holidaysRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException exception) {
            return false;
        }
    }

public List<Holidays> searchByNameAndDate(String name, String from, String to) throws ParseException {
        if(name == null|| name.equals("")){
            if(from == null|| from.equals("") && to == null|| to.equals("")){
                    int yearNow = LocalDate.now().getYear();
                    return holidaysRepository.findAllHolidays(yearNow);
            }
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);
            return  holidaysRepository.findAllByStartsLessThanEqualAndEndsGreaterThanEqual(endDate, startDate);
        } else if (from == null|| from.equals("") && to == null|| to.equals("")){
            return  holidaysRepository.findAllByNameIgnoreCaseContaining(name);
        }
    Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
    Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);
        return  holidaysRepository.findAllByNameIgnoreCaseContainingOrStartsLessThanEqualAndEndsGreaterThanEqual(name, endDate, startDate);
}
}
