package lt.techin.springyne.holiday;

import lt.techin.springyne.exception.ScheduleValidationException;
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

    private Holiday getHolidayById(Long id) {
        return holidaysRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Holiday does not exist", "id",
                        "Holiday not found", String.valueOf(id)));
    }

    private void checkHolidayNameEmpty(String name) {
        if (name == null || name.equals("")) {
            throw new ScheduleValidationException("Holiday name cannot be empty", "name",
                    "Name is empty", name);
        }
    }

    public HolidayService(HolidaysRepository holidaysRepository) {
        this.holidaysRepository = holidaysRepository;
    }

    public List<Holiday> getAll() {

        return holidaysRepository.findAll();
    }

    public boolean delete(Long id) {

        try {
            holidaysRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException exception) {
            return false;
        }
    }

    public List<Holiday> searchByNameAndDate(String name, String from, String to) throws ParseException {
        if (name == null || name.equals("")) {
            if (from == null || from.equals("") && to == null || to.equals("")) {
                int yearNow = LocalDate.now().getYear();
                return holidaysRepository.findAllHolidays(yearNow);
            }
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);
            return holidaysRepository.findAllByStartsLessThanEqualAndEndsGreaterThanEqualOrderByStartsAsc(endDate, startDate);
        } else if (from == null || from.equals("") && to == null || to.equals("")) {
            return holidaysRepository.findAllByNameIgnoreCaseContainingOrderByStartsAsc(name);
        }
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);
        return holidaysRepository.findAllByNameIgnoreCaseContainingOrStartsLessThanEqualAndEndsGreaterThanEqualOrderByStartsAsc(name, endDate, startDate);
    }

    public Holiday createHoliday(Holiday holiday) {
        Holiday newHoliday = new Holiday();
        if (holiday.getName() == null ||holiday.getName().isEmpty()){
            throw new ScheduleValidationException("Name cannot be empty", "name",
                    "Name is empty", holiday.getName());
        }
           if ( holiday.getStarts() == null){
               throw new ScheduleValidationException("Start date cannot be empty", "date",
                       "Date is empty", "Start date");
           }
           if( holiday.getEnds() == null) {
            throw new ScheduleValidationException("End date cannot be empty", "date",
                    "Date is empty", "End date");
        }

//           newHoliday.setName(holiday.getName());
//           newHoliday.setStarts(holiday.getStarts());
//           newHoliday.setEnds(holiday.getEnds());
//           newHoliday.setRepeats(holiday.isRepeats());

        return holidaysRepository.save(holiday);
    }


}
