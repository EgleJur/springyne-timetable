package lt.techin.springyne.holiday;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HolidayMapper {

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static Holiday toHoliday(HolidayDto holidayDto) {

        Holiday holiday = new Holiday();

        holiday.setName(holidayDto.getName());
        //try {
            holiday.setStarts(LocalDate.parse(holidayDto.getStarts()));
//        } catch (ParseException e) {
//            throw new ScheduleValidationException("Start date cannot be converted to date", "date",
//                    "Wrong format of date", holidayDto.getStarts());
//        }
       // try {
            holiday.setEnds(LocalDate.parse(holidayDto.getEnds()));
//        } catch (ParseException e) {
//            throw new ScheduleValidationException("End date cannot be converted to date", "date",
//                    "Wrong format of date", holidayDto.getEnds());
//        }
        holiday.setRepeats(holidayDto.isRepeats());

        return holiday;

    }

    public static HolidayDto toHolidayDto(Holiday holiday) {

        HolidayDto holidayDto = new HolidayDto();

        holidayDto.setName(holiday.getName());
        holidayDto.setStarts(dateFormatter.format(holiday.getStarts()));
        holidayDto.setEnds(dateFormatter.format(holiday.getEnds()));
        holidayDto.setRepeats(holiday.isRepeats());

        return holidayDto;
    }


}