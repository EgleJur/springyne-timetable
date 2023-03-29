package lt.techin.springyne.holiday;

import lt.techin.springyne.exception.ScheduleValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HolidayService {
    @Autowired
    private final HolidaysRepository holidaysRepository;

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
        List<Holiday> allHolidays = holidaysRepository.findAll();
        List<Holiday> newHolidayList = new ArrayList<>();
        if (name == null || name.equals("")) {
            if (from == null || from.equals("") && to == null || to.equals("")) {
                int yearNow = LocalDate.now().getYear();
                LocalDate startDate = LocalDate.now().plusYears(-1);
                LocalDate endDate = LocalDate.now().plusYears(2);

                List<Holiday> sortedHolidayList = createListFromRepeats(startDate, endDate, allHolidays);
                return sortedHolidayList.stream()
                        .filter(holidayDate -> holidayDate.getStarts().getYear() == yearNow || holidayDate.getEnds().getYear() == yearNow)
                        .collect(Collectors.toList());
            } else {
                LocalDate startDate = LocalDate.parse(from);
                LocalDate endDate = LocalDate.parse(to);

                List<Holiday> sortedHolidayList = createListFromRepeats(startDate, endDate, allHolidays);

                return sortedHolidayList.stream()
                        .filter(dateS -> dateS.getEnds().isAfter(startDate.minusDays(1))
                                && dateS.getStarts().isBefore(endDate.plusDays(1)))
                        .collect(Collectors.toList());

            }
        } else if (from == null || from.equals("") && to == null || to.equals("")) {
            int yearNow = LocalDate.now().getYear();
            LocalDate startDate = LocalDate.now().plusYears(-1);
            LocalDate endDate = LocalDate.now().plusYears(2);

            List<Holiday> filteredHoliday = holidaysRepository.findAllByNameIgnoreCaseContaining(name);
            List<Holiday> sortedHolidayList = createListFromRepeats(startDate, endDate, filteredHoliday);
            return sortedHolidayList.stream()
                    .filter(holidayDate -> holidayDate.getStarts().getYear() == yearNow || holidayDate.getEnds().getYear() == yearNow)
                    .collect(Collectors.toList());
        } else {
            LocalDate startDate = LocalDate.parse(from);
            LocalDate endDate = LocalDate.parse(to);

            List<Holiday> filteredHoliday = holidaysRepository.findAllByNameIgnoreCaseContaining(name);

              List<Holiday> sortedHolidayList = createListFromRepeats(startDate, endDate, filteredHoliday);
            return sortedHolidayList.stream()
                    .filter(dateS -> dateS.getEnds().isAfter(startDate.minusDays(1))
                            && dateS.getStarts().isBefore(endDate.plusDays(1)))
                    .collect(Collectors.toList());

        }
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
        if (holidaysRepository.findAllByNameIgnoreCase(holiday.getName(), holiday.getStarts(), holiday.getEnds())) {
            throw new ScheduleValidationException("Holiday exists", "name",
                    "Holiday exists", holiday.getName());
        }

        return holidaysRepository.save(holiday);
    }

    private List<Holiday> createListFromRepeats(LocalDate startDate, LocalDate endDate, List<Holiday> filteredHoliday) {
        List<Holiday> newHolidayList = new ArrayList<>();
        for (Holiday holiday : filteredHoliday) {

            if (holiday.isRepeats()) {
                LocalDate holidayStarts = holiday.getStarts();
                LocalDate holidayEnds = holiday.getEnds();
                int holidayStartYear = holidayStarts.getYear();
                int holidayEndYear = holidayEnds.getYear();
                LocalDate newHolidayEnds = holidayEnds;

                for (int year = startDate.getYear(); year <= endDate.getYear(); year++) {
                    LocalDate newHolidayStarts = holidayStarts.plusYears(year - holidayStartYear);
                    if (holidayStartYear < holidayEndYear) {

                        newHolidayEnds = holidayEnds.plusYears(year - holidayEndYear + 1);
                    } else {
                        newHolidayEnds = holidayEnds.plusYears(year - holidayEndYear);
                    }
                    Holiday newHoliday = new Holiday();
                    newHoliday.setName(holiday.getName());
                    newHoliday.setStarts(newHolidayStarts);
                    newHoliday.setEnds(newHolidayEnds);
                    newHoliday.setId(holiday.getId());
                    newHolidayList.add(newHoliday);
                }
            } else {
                newHolidayList.add(holiday);
            }
        }

        return newHolidayList.stream()
                .sorted(Comparator.comparing(Holiday::getStarts))
                .collect(Collectors.toList());

    }

}
