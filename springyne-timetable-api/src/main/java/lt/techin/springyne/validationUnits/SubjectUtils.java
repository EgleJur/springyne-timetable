package lt.techin.springyne.validationUnits;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.subject.Subject;
import lt.techin.springyne.subject.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class SubjectUtils {

    @Autowired
    SubjectRepository subjectRepository;

    public SubjectUtils(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist", "id",
                        "Subject not found", String.valueOf(id)));
    }

    public boolean existsByName(String name) {
        return subjectRepository.existsByNameIgnoreCase(name);
    }

    public void checkSubjectNameUnique(String name) {
        if (existsByName(name)) {
            throw new ScheduleValidationException("Subject name must be unique",
                    "name", "Name already exists", name);
        }
    }
}
