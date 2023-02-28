package lt.techin.springyne.controller;

import lt.techin.springyne.shift.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class ShiftControllerMockDbTest {

    @MockBean
    ShiftRepository shiftRepository;

    @Autowired
    ShiftService shiftService;

    @Test
    void addShiftReturnsSavedShift() {
        ShiftDto testShiftDto = new ShiftDto(LocalDateTime.now().toString(),1,7,1);
        Shift testShift = ShiftMapper.toShift(testShiftDto);
        Mockito.when(shiftRepository.save(testShift)).thenReturn(testShift);
        assertEquals(testShift, shiftService.createShift(testShift), "Should be able to add new Shift with unique number");

    }

}