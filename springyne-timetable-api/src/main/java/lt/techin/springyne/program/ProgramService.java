package lt.techin.springyne.program;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.subject.Subject;
import lt.techin.springyne.subject.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        if (program.getName() == null || program.getName().equals("")) {
            throw new ScheduleValidationException("Program name cannot be empty", "name", "Name is empty", program.getName());
        }
        if (program.getDescription().equals("") || program.getDescription() == null) {
            throw new ScheduleValidationException("Program description cannot be empty", "description", "Description is empty",
                    program.getDescription());
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

    public Program deleteProgram(Long programId) {
        Program programToDelete = programRepository.findById(programId).orElseThrow(
                () -> new ScheduleValidationException("Program does not exist", "id", "Program not found", String.valueOf(programId)));
        if (!programToDelete.isDeleted()) {
            programToDelete.setDeleted(true);
            return programRepository.save(programToDelete);
        } else {
            return programToDelete;
        }
    }

    public Program restoreProgram(Long programId) {
        Program programToRestore = programRepository.findById(programId).orElseThrow(
                () -> new ScheduleValidationException("Program does not exist", "id", "Program not found", String.valueOf(programId)));
        if (programToRestore.isDeleted()) {
            programToRestore.setDeleted(false);
            return programRepository.save(programToRestore);
        } else {
            return programToRestore;
        }
    }

    public Program removeSubjectFromProgram(Long programId, Long subjectId) {

        Program programToUpdate = programRepository.findById(programId).orElseThrow(() -> new ScheduleValidationException("Program does not exist",
                "programId", "Program not found", String.valueOf(programId)));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist", "subjectId", "Subject not found",
                        String.valueOf(subjectId)));
        ProgramSubject programSubjectToRemove = programToUpdate.getSubjects().stream().filter(
                (ProgramSubject programSubject) -> programSubject.getSubject().equals(subject)).findFirst().orElseThrow(
                () -> new ScheduleValidationException("Subject does not exist in this program", "subjectId", "Program subject not found",
                        String.valueOf(subjectId)));
        programToUpdate.getSubjects().remove(programSubjectToRemove);

        return programRepository.save(programToUpdate);
    }

    public Program updateProgram(Long programId, Program program, Long subjectId, Integer hours) {

        Program programToUpdate = programRepository.findById(programId).orElseThrow(() -> new ScheduleValidationException("Program does not exist",
                "programId", "Program not found", String.valueOf(programId)));

        if (program.getName().equals("") || program.getName() == null) {
            throw new ScheduleValidationException("Program name cannot be empty", "name", "Name is empty", program.getName());
        } else {
            programToUpdate.setName(program.getName());
        }

        if (program.getDescription().equals("") || program.getDescription() == null) {
            throw new ScheduleValidationException("Program description cannot be empty", "description", "Description is empty",
                    program.getDescription());
        } else {
            programToUpdate.setDescription(program.getDescription());
        }


        if ( !((subjectId == null) && (hours == null))){
            if (subjectId != null && hours != null) {
                Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new ScheduleValidationException(
                        "Subject does not exist", "SubjectId", "Subject not found", String.valueOf(subjectId)));
                if (hours < 0) {
                    throw new ScheduleValidationException(
                            "Hours must be a positive integer", "hours", "Hours < 0", String.valueOf(hours));
                }
                ProgramSubject existingProgramSubject = programToUpdate.getSubjects().stream().filter(
                        (ProgramSubject programSubject) -> programSubject.getSubject().equals(subject)).findFirst().orElse(null);
                if (existingProgramSubject == null) {
                    ProgramSubject programSubject = new ProgramSubject(subject, hours);
                    programToUpdate.getSubjects().add(programSubject);
                } else {
                    existingProgramSubject.setHours(hours);
                }
            }
            if (subjectId == null) {
                throw new ScheduleValidationException("Cannot set hours to empty subject", "SubjectId",
                        "Subject not found", String.valueOf(subjectId));
            }
            if (hours == null) {
                throw new ScheduleValidationException("Cannot add subject with empty hours", "hours",
                        "Hours is empty", String.valueOf(hours));
            }
        }

        return programRepository.save(programToUpdate);
    }
}
