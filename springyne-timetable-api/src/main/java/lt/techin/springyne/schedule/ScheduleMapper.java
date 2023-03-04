package lt.techin.springyne.schedule;

public class ScheduleMapper {

    public static Schedule toSchedule(ScheduleDto scheduleDto) {

        Schedule schedule = new Schedule();
        schedule.setName(scheduleDto.getName());
        schedule.setStartDate(scheduleDto.getStartDate());
        schedule.setEndDate(scheduleDto.getEndDate());

        return schedule;
    }

    public static ScheduleDto toScheduleDto (Schedule schedule) {

        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setName(schedule.getName());
        scheduleDto.setStartDate(schedule.getStartDate());
        scheduleDto.setEndDate(schedule.getEndDate());

        return scheduleDto;
    }
}
