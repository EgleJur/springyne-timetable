package lt.techin.springyne.service;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.model.Program;
import lt.techin.springyne.model.ProgramSubject;
import lt.techin.springyne.repository.ProgramRepository;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgramService {

    @Autowired
    ProgramRepository programRepository;

    @Autowired
    SubjectRepository subjectRepository;


    public ProgramService(ProgramRepository programRepository, SubjectRepository subjectRepository) {
        this.programRepository = programRepository;
        this.subjectRepository = subjectRepository;
    }


    public List<Program> getAllModules() {
        return programRepository.findAll();
    }

    public Program createProgram(Program program, Long subjectId, Integer hours) {

        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new ScheduleValidationException(
                "Subject does not exist", "id", "Subject not found", String.valueOf(subjectId)));
        if (hours < 0) {
            throw new ScheduleValidationException(
                    "Hours must be a positive integer", "hours", "Hours < 0", String.valueOf(hours));
        }
        if (program.getName().equals("") || program.getName() == null) {
            throw new ScheduleValidationException("Program name cannot be empty", "name", "Name is empty", program.getName());
        }
        ProgramSubject programSubject = new ProgramSubject(subject, hours);
        program.getSubjects().add(programSubject);
        return programRepository.save(program);
    }

    public Optional<Program> getProgramById(Long programId) {
        return programRepository.findById(programId);
    }

    public Page<Program> searchByName(String name, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by("deleted").and(Sort.by("id")));
        return programRepository.findAllByNameIgnoreCaseContaining(name,pageable);
    }
}
