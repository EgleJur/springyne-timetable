package lt.techin.springyne.controller;

import lt.techin.springyne.model.Holidays;
import lt.techin.springyne.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

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

    @PatchMapping("/delete/{holidayId}")
    public ResponseEntity<Holidays> deleteHolidays(@PathVariable Long holidayId) {

        var updatedHolidays = holidayService.delete(holidayId);
        return ok(updatedHolidays);

    }
    //
    @PatchMapping("/restore/{holidayId}")
    public ResponseEntity<Holidays> restoreHolidays(@PathVariable Long holidayId) {

        var restoredHolidays = holidayService.restore(holidayId);
        return ok(restoredHolidays);

    }
}
