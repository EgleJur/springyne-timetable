package lt.techin.springyne.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @PostMapping
    public ResponseEntity<Schedule> addSchedule(@Valid @RequestBody ScheduleDto scheduleDto,
                                                @RequestParam Long groupId) {
        return ResponseEntity.ok(scheduleService.addSchedule(ScheduleMapper.toSchedule(scheduleDto), groupId));
    }

    @GetMapping("/{scheduleId}")
    public Optional<Schedule> getScheduleById(@PathVariable Long scheduleId) {
        return scheduleService.getScheduleById(scheduleId);
    }

    @GetMapping("/search")
    public Page<Schedule> filterSchedulesByNameDatePaged(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDate date,
            @RequestParam int page,
            @RequestParam int pageSize) {

        return scheduleService.searchByNameDatePaged(name, date, page, pageSize);
    }

    @DeleteMapping("/delete/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {

        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok().build();
    }
}
