package lt.techin.springyne.holiday;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lt.techin.springyne.holiday.HolidayMapper.toHoliday;
import static lt.techin.springyne.holiday.HolidayMapper.toHolidayDto;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/holidays")
public class HolidayController {
    @Autowired
    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping("/all")
    public List<Holiday> getAll(){
        return holidayService.getAll();
    }

    @PatchMapping("/delete/{holidayId}")
    public boolean deleteHolidays(@PathVariable Long holidayId) {

        return holidayService.delete(holidayId);

    }

    @GetMapping("/search")
    public List<Holiday> searchByDate(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String from,
                                      @RequestParam(required = false) String to) {
        return holidayService.searchByNameAndDate(name, from, to);
    }

    @PostMapping("/createHoliday")
    public ResponseEntity<HolidayDto> createHoliday(@RequestBody HolidayDto holidayDto) {
        Holiday createdHoliday = holidayService.createHoliday(toHoliday(holidayDto));
        return ok(toHolidayDto(createdHoliday));
    }
}
