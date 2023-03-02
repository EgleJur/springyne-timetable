package lt.techin.springyne.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/schedules")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

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
}
