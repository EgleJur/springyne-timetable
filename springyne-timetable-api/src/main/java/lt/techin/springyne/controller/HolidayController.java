package lt.techin.springyne.controller;

import lt.techin.springyne.model.Holidays;
import lt.techin.springyne.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/holidays")
public class HolidayController {
    @Autowired
    HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public List<Holidays> getAllHolidays(){
        return holidayService.getAllHolidays();
    }
    @GetMapping("/all")
    public List<Holidays> getAll(){
        return holidayService.getAll();
    }

    @PatchMapping("/delete/{holidayId}")
    public boolean deleteHolidays(@PathVariable Long holidayId) {

        return holidayService.delete(holidayId);

    }

    @GetMapping("/search")
    public List<Holidays> searchByDate(@RequestParam(required = false) String name,
                                       @RequestParam(required = false) String from,
                                           @RequestParam(required = false) String to) throws ParseException {
        return holidayService.searchByNameAndDate(name, from, to);
    }
}
