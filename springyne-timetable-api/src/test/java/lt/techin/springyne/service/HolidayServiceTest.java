package lt.techin.springyne.service;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.holiday.Holiday;
import lt.techin.springyne.holiday.HolidayService;
import lt.techin.springyne.holiday.HolidaysRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HolidayServiceTest {

    @Mock
    private HolidaysRepository holidaysRepository;

    @InjectMocks
    private HolidayService holidayService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        List<Holiday> holidays = new ArrayList<>();
        holidays.add(new Holiday(1L,"New Year's Day", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1), false));
        holidays.add(new Holiday(2L,"Christmas Day", LocalDate.of(2023, 12, 25), LocalDate.of(2023, 12, 25), false));
        Mockito.when(holidaysRepository.findAll()).thenReturn(holidays);

        List<Holiday> result = holidayService.getAll();

        assertEquals(holidays.size(), result.size());
        assertEquals(holidays.get(0), result.get(0));
        assertEquals(holidays.get(1), result.get(1));
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        Mockito.doNothing().when(holidaysRepository).deleteById(id);

        boolean result = holidayService.delete(id);

        Assertions.assertTrue(result);
    }

    @Test
    public void testDeleteWhenIdDoesNotExist() {
        // given
        Long idToDelete = 1L;
        doThrow(new EmptyResultDataAccessException(1)).when(holidaysRepository).deleteById(idToDelete);

        // when
        boolean result = holidayService.delete(idToDelete);

        // then
        assertFalse(result);
        verify(holidaysRepository, times(1)).deleteById(idToDelete);
    }
    @Test
    void testSearchByNameAndDate() {
        Holiday holiday1 = new Holiday(1L,"Christmas", LocalDate.of(2023, 12, 25), LocalDate.of(2023, 12, 26), false);
        Holiday holiday2 = new Holiday(2L,"New Year's Day", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 2), false);
        Holiday holiday3 = new Holiday(3L,"Easter", LocalDate.of(2023, 4, 16), LocalDate.of(2023, 4, 17), false);
        List<Holiday> holidays = Arrays.asList(holiday1, holiday2, holiday3);
        when(holidaysRepository.findAllOrderByStartsAsc()).thenReturn(holidays);

        List<Holiday> result = holidayService.searchByNameAndDate(null, null, null);
        assertEquals(holidays, result);
    }

    @Test
    public void searchByNameAndDate_WhenNameIsNullAndFromAndToAreSpecified_ReturnsFilteredHolidays() {
        // Create some holiday data for testing
        Holiday holiday1 = new Holiday(1L, "Christmas", LocalDate.of(2023, 12, 25), LocalDate.of(2023, 12, 25), false);
        Holiday holiday2 = new Holiday(2L, "New Year's Day", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1), false);
        Holiday holiday3 = new Holiday(3L, "Labor Day", LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 1), false);
        Holiday holiday4 = new Holiday(4L, "Easter", LocalDate.of(2023, 4, 16), LocalDate.of(2023, 4, 16), false);

        LocalDate from = LocalDate.parse("2023-01-01");
        LocalDate to = LocalDate.parse("2023-12-31");
        // Mock the holidays repository to return the holiday data for testing
        List<Holiday> allHolidays = new ArrayList<>();
        allHolidays.add(holiday1);
        allHolidays.add(holiday2);
        allHolidays.add(holiday3);
        allHolidays.add(holiday4);
        when(holidaysRepository.findAll()).thenReturn(allHolidays);
        List<Holiday> sortedallHolidays = allHolidays.stream()
                .sorted(Comparator.comparing(Holiday::getStarts))
                .collect(Collectors.toList());
        List<Holiday> allHolidaysS = sortedallHolidays.stream()
                .filter(dateS -> dateS.getEnds().isAfter(from.minusDays(1))
                        && dateS.getStarts().isBefore(to.plusDays(1)))
                .collect(Collectors.toList());
        // Call the method being tested
        List<Holiday> result = holidayService.searchByNameAndDate(null, "2023-01-01", "2023-12-31");
        assertEquals(allHolidaysS, result);
    }

    @Test
    public void searchByNameAndDate_WhenNameIsSpecifiedAndFromAndToAreNull_ReturnsFilteredHolidays() {
        // Create some holiday data for testing
        Holiday holiday1 = new Holiday(1L, "Christmas", LocalDate.of(2023, 12, 25), LocalDate.of(2023, 12, 25), false);
        Holiday holiday2 = new Holiday(2L, "New Year's Day", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1), false);
        Holiday holiday3 = new Holiday(3L, "Labor Day", LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 1), false);
        Holiday holiday4 = new Holiday(4L, "Easter", LocalDate.of(2023, 4, 16), LocalDate.of(2023, 4, 16), false);

        LocalDate from =LocalDate.now().plusYears(-1);
        LocalDate to =LocalDate.now().plusYears(2);


        // Mock the holidays repository to return the holiday data for testing
        List<Holiday> allHolidays = new ArrayList<>();
        allHolidays.add(holiday1);
        allHolidays.add(holiday2);
        allHolidays.add(holiday3);
        allHolidays.add(holiday4);
        when(holidaysRepository.findAllByNameIgnoreCaseContaining("Christmas")).thenReturn(allHolidays);
        List<Holiday> sortedallHolidays = allHolidays.stream()
                .sorted(Comparator.comparing(Holiday::getStarts))
                .collect(Collectors.toList());

        // Call the method being tested
        List<Holiday> result = holidayService.searchByNameAndDate("Christmas", from.toString(), to.toString());
        assertEquals(sortedallHolidays, result);
    }
    @Test
    public void searchByNameAndDate_WhenNameIsSpecifiedAndFromAndToAreSpecified_ReturnsFilteredHolidays() {
        // Create some holiday data for testing
        Holiday holiday1 = new Holiday(1L, "Christmas", LocalDate.of(2023, 12, 25), LocalDate.of(2023, 12, 25), false);
        Holiday holiday2 = new Holiday(2L, "New Year's Day", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1), false);
        Holiday holiday3 = new Holiday(3L, "Labor Day", LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 1), false);
        Holiday holiday4 = new Holiday(4L, "Easter", LocalDate.of(2023, 4, 16), LocalDate.of(2023, 4, 16), false);

        LocalDate from = LocalDate.parse("2023-01-01");
        LocalDate to = LocalDate.parse("2023-12-31");
        // Mock the holidays repository to return the holiday data for testing
        List<Holiday> allHolidays = new ArrayList<>();
        allHolidays.add(holiday1);
        allHolidays.add(holiday2);
        allHolidays.add(holiday3);
        allHolidays.add(holiday4);
        when(holidaysRepository.findAllByNameIgnoreCaseContaining("Christmas")).thenReturn(allHolidays);
        List<Holiday> sortedallHolidays = allHolidays.stream()
                .sorted(Comparator.comparing(Holiday::getStarts))
                .collect(Collectors.toList());
        sortedallHolidays.stream()
                .filter(dateS -> dateS.getEnds().isAfter(from.minusDays(1))
                        && dateS.getStarts().isBefore(to.plusDays(1)))
                .collect(Collectors.toList());
        // Call the method being tested
        List<Holiday> result = holidayService.searchByNameAndDate("Christmas", "2023-01-01", "2023-12-31");
        assertEquals(sortedallHolidays, result);
    }

    @Test
    public void testCreateHoliday() {
        // Given
        Holiday holiday = new Holiday(1L,"New Year's Day", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1), false);
        Mockito.when(holidaysRepository.findAllByNameIgnoreCase(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
                .thenReturn(false);
        Mockito.when(holidaysRepository.save(Mockito.any(Holiday.class))).thenReturn(holiday);

        // When
        Holiday savedHoliday = holidayService.createHoliday(holiday);

        // Then
        assertEquals(holiday, savedHoliday);
    }
    @Test
    public void testCreateHolidayWithEmptyName() {
        // Given
        Holiday holiday = new Holiday(1L,"", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1), false);

        ScheduleValidationException exception = assertThrows(ScheduleValidationException.class, () -> holidayService.createHoliday(holiday));
        assertEquals("Name cannot be empty", exception.getMessage());
    }
    @Test
    public void testCreateHolidayWithEmptyStartDate() {
        // Given
        Holiday holiday = new Holiday(1L,"New Year's Day", null, LocalDate.of(2023, 1, 1), false);

        ScheduleValidationException exception = assertThrows(ScheduleValidationException.class, () -> holidayService.createHoliday(holiday));
        assertEquals("Start date cannot be empty", exception.getMessage());
    }

    @Test
    public void testCreateHolidayWithEmptyEndDate() {
        // Given
        Holiday holiday = new Holiday(1L,"New Year's Day", LocalDate.of(2023, 1, 1), null, false);

        ScheduleValidationException exception = assertThrows(ScheduleValidationException.class, () -> holidayService.createHoliday(holiday));
        assertEquals("End date cannot be empty", exception.getMessage());
    }

    @Test
    public void testCreateHolidayWithDuplicateNameAndOverlappingDates() {
        // Given
        Holiday existingHoliday = new Holiday(1L,"New Year's Day", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1), false);
        Mockito.when(holidaysRepository.findAllByNameIgnoreCase(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
                .thenReturn(true);

        Holiday holiday = new Holiday(2L,"New Year's Day", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1), false);

        ScheduleValidationException exception = assertThrows(ScheduleValidationException.class, () -> holidayService.createHoliday(holiday));
        assertEquals("Holiday exists", exception.getMessage());
    }
}