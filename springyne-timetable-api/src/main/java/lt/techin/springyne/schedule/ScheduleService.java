package lt.techin.springyne.schedule;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.group.Group;
import lt.techin.springyne.group.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    GroupRepository groupRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, GroupRepository groupRepository) {
        this.scheduleRepository = scheduleRepository;
        this.groupRepository = groupRepository;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule addSchedule(Schedule schedule, Long groupId) {
        if (schedule.getName() == null || schedule.getName().equals("")) {
            throw new ScheduleValidationException("Schedule name cannot be empty", "name", "Name is empty",
                    schedule.getName());
        }
        if (schedule.getStartDate() == null) {
            throw new ScheduleValidationException("Schedule start date cannot be empty", "start date",
                    "Start date is empty", schedule.getStartDate().toString());
        }
        if (schedule.getEndDate() == null) {
            throw new ScheduleValidationException("Schedule end date cannot be empty", "end date",
                    "End date is empty", schedule.getEndDate().toString());
        }
        if (schedule.getEndDate().isBefore(schedule.getStartDate())) {
            throw new ScheduleValidationException("Schedule end date cannot be before start date", "end date",
                    "End date is before start date", schedule.getEndDate().toString());
        }
        if (schedule.getStartDate().isBefore(LocalDate.now())) {
            throw new ScheduleValidationException("Schedule start date cannot be before current date", "start date",
                    "Start is before current date", schedule.getStartDate().toString());
        }

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ScheduleValidationException(
                "Group does not exist", "id", "Group not found", String.valueOf(groupId)));
        schedule.setGroup(group);

        return scheduleRepository.save(schedule);
    }
}
