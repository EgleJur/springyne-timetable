package lt.techin.springyne.holiday;

import lt.techin.springyne.exception.ScheduleValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Holiday> searchByNameAndDate(String name, String from, String to) {
        List<Holiday> allHolidays = holidaysRepository.findAllOrderByStartsAsc();
        List<Holiday> newHolidayList = new ArrayList<>();
        if (name == null || name.equals("")) {
            if (from == null || from.equals("") && to == null || to.equals("")) {
                int yearNow = LocalDate.now().getYear();
                return allHolidays.stream()
                        .filter(startDate -> startDate.getStarts().getYear() == yearNow)
                        .collect(Collectors.toList());
            }
            LocalDate startDate = LocalDate.parse(from);
            LocalDate endDate = LocalDate.parse(to);
            List<Holiday> filteredHoliday = holidaysRepository.findAllHolidaysByDate(startDate, endDate);
            // List<Holiday> newHolidayList = new ArrayList<>();
            for (Holiday holiday : filteredHoliday) {
                if (holiday.isRepeats()) {

                    holiday.setStarts(holiday.getStarts().plusYears(startDate.getYear() - holiday.getStarts().getYear()));
                    holiday.setEnds(holiday.getEnds().plusYears(startDate.getYear() - holiday.getEnds().getYear()));
                }
                newHolidayList.add(holiday);
            }
            return newHolidayList.stream()
                    .filter(dateS -> dateS.getStarts().isAfter(startDate)
                            && dateS.getEnds().isBefore(endDate)
                            || dateS.getStarts().isEqual(startDate)
                            || dateS.getEnds().isEqual(endDate))
                    .collect(Collectors.toList());

        } else if (from == null || from.equals("") && to == null || to.equals("")) {
            return holidaysRepository.findAllByNameIgnoreCaseContainingOrderByStartsAsc(name);
        }
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);
        List<Holiday> filteredHoliday = holidaysRepository.findAllHolidaysByDateAndName(name, endDate, startDate);

        for (Holiday holiday : filteredHoliday) {

            if (holiday.isRepeats()) {
                long i = startDate.getYear() - holiday.getStarts().getYear();
                holiday.setStarts(holiday.getStarts().plusYears(i));
                holiday.setEnds(holiday.getEnds().plusYears(i));
            }
            newHolidayList.add(holiday);
        }
        return newHolidayList.stream()
                .filter(dateS -> dateS.getStarts().isAfter(startDate)
                        && dateS.getEnds().isBefore(endDate)
                        || dateS.getStarts().isEqual(startDate)
                        || dateS.getEnds().isEqual(endDate))
                .collect(Collectors.toList());
        // return holidaysRepository.findAllHolidaysByDateAndName(name, endDate, startDate);
    }

    public Holiday createHoliday(Holiday holiday) {


        if (holiday.getName() == null || holiday.getName().isEmpty()) {
            throw new ScheduleValidationException("Name cannot be empty", "name",
                    "Name is empty", holiday.getName());
        }
        if (holiday.getStarts() == null) {
            throw new ScheduleValidationException("Start date cannot be empty", "date",
                    "Date is empty", "Start date");
        }
        if (holiday.getEnds() == null) {
            throw new ScheduleValidationException("End date cannot be empty", "date",
                    "Date is empty", "End date");
        }
//        holidaysRepository.findAllByNameIgnoreCase(holiday.getName());
        if(holidaysRepository.findAllByNameIgnoreCase(holiday.getName(), holiday.getStarts(), holiday.getEnds())){
            throw new ScheduleValidationException("Holiday exists", "name",
                    "Holiday exists", holiday.getName());
        }

        return holidaysRepository.save(holiday);
    }


}
