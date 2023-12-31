package lt.techin.springyne.controller;

import lt.techin.springyne.holiday.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static lt.techin.springyne.holiday.HolidayMapper.toHoliday;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HolidayControllerTest {
    @Mock
    private HolidayService holidayService;

    @InjectMocks
    private HolidayController holidayController;

    @Test
    void testGetAll() {
        List<Holiday> expected = new ArrayList<>();
        expected.add(toHoliday(new HolidayDto("Holiday 1", LocalDate.of(2023,5,5), LocalDate.of(2023,5,10),false)));
        expected.add(toHoliday(new HolidayDto("Holiday 2", LocalDate.of(2023,4,5),LocalDate.of(2023,4,19),false)));
        when(holidayService.getAll()).thenReturn(expected);

        List<Holiday> actual = holidayController.getAll();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testDeleteHoliday() {
        Long holidayId = 1L;
        when(holidayService.delete(holidayId)).thenReturn(true);

        boolean actual = holidayController.deleteHolidays(holidayId);

        assertThat(actual).isTrue();
    }

    @Test
    void testSearchByDate() throws ParseException {
        List<Holiday> expected = new ArrayList<>();
        expected.add(toHoliday(new HolidayDto("Holiday 1", LocalDate.of(2023,5,5), LocalDate.of(2023,5,10),false)));
        when(holidayService.searchByNameAndDate(null, "2023-01-01", null)).thenReturn(expected);

        List<Holiday> actual = holidayController.searchByDate(null, "2023-01-01", null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testCreateHoliday() {
        HolidayDto holidayDto = new HolidayDto();
        holidayDto.setName("New Year's Day");
        holidayDto.setStarts(LocalDate.of(2024,1,1));
        holidayDto.setEnds(LocalDate.of(2024,1,2));
        holidayDto.setRepeats(false);
        Holiday expected = (toHoliday(new HolidayDto(holidayDto.getName(),
                holidayDto.getStarts(), holidayDto.getEnds(), holidayDto.isRepeats())));
        when(holidayService.createHoliday(toHoliday(holidayDto))).thenReturn(expected);

        ResponseEntity<HolidayDto> actual = holidayController.createHoliday(holidayDto);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEqualTo(HolidayMapper.toHolidayDto(expected));
    }
}
