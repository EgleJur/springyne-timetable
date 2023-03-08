package lt.techin.springyne.schedule;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.group.Group;
import lt.techin.springyne.group.GroupRepository;
import lt.techin.springyne.module.Module;
import lt.techin.springyne.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

        List <Schedule> existingGroupSchedules = scheduleRepository.findByGroupId(groupId);

        Schedule overlappingSchedule = existingGroupSchedules.stream().filter((existingSchedule) -> checkOverlappingDateRange(
                existingSchedule.getStartDate(), existingSchedule.getEndDate(), schedule.getStartDate(), schedule.getEndDate())).
                findFirst().orElse(null);
        if (overlappingSchedule != null) {
            throw new ScheduleValidationException("Schedule start and/or end date cannot overlap with the current schedules of this group",
                    "start - end date", "Dates overlap", (schedule.getStartDate().toString() + " - " + schedule.getEndDate().toString()));
        }

        schedule.setGroup(group);

        return scheduleRepository.save(schedule);
    }

    public boolean checkOverlappingDateRange(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        return (startDate1.isBefore(endDate2) && startDate1.isAfter(startDate2)) || (endDate1.isBefore(endDate2) && endDate1.isAfter(startDate2))
                || (startDate2.isBefore(endDate1) && startDate2.isAfter(startDate1)) || (endDate2.isBefore(endDate1) && endDate2.isAfter(endDate1))
                || startDate1.isEqual(startDate2) || startDate1.isEqual(endDate2) || endDate1.isEqual(startDate2) || endDate1.isEqual(endDate2);
    }

    public Optional<Schedule> getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }

    public Page<Schedule> searchByNameDatePaged(String name, LocalDate date, int page, int pageSize) {
        if (name==null) {
            name = "";
        }
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by("startDate").descending().and(Sort.by("id")));

        if (date == null) {
            return scheduleRepository.findAllByNameIgnoreCaseContaining(name, pageable);
        } else {
            return scheduleRepository.findAllByNameIgnoreCaseContainingAndEndDateGreaterThanEqual(name, date, pageable);
        }
    }

    public boolean delete(Long scheduleId) {
        try {
                scheduleRepository.deleteById(scheduleId);
                return true;
        } catch (EmptyResultDataAccessException exception) {
            return false;
        }
    }

}