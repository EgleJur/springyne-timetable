package lt.techin.springyne.controller;

import lt.techin.springyne.group.GroupRepository;
import lt.techin.springyne.schedule.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleControllerMockDbTest {

    @MockBean
    ScheduleRepository scheduleRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ScheduleService scheduleService;

    @Test
    void addScheduleReturnsSavedSchedule() {
        ScheduleDto testScheduleDto = new ScheduleDto("Test schedule", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        Schedule testSchedule = ScheduleMapper.toSchedule(testScheduleDto);
        testSchedule.setGroup(groupRepository.findById(1L).get());
        Mockito.when(scheduleRepository.save(testSchedule)).thenReturn(testSchedule);
        assertEquals(testSchedule, scheduleService.addSchedule(testSchedule,1L),
                "Should be able to add new Schedule with correct days");
    }
}
