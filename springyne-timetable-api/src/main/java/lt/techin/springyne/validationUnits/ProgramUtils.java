package lt.techin.springyne.validationUnits;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.program.Program;
import lt.techin.springyne.program.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class ProgramUtils {

    @Autowired
    ProgramRepository programRepository;

    public ProgramUtils(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public Program getProgramById(Long id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Program does not exist", "id",
                        "Program not found", String.valueOf(id)));
    }


}
