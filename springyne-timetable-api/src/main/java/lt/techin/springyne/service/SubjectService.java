package lt.techin.springyne.service;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAll() {
        return subjectRepository.findAllSubjects();

    }

    public Optional<Subject> getById(Long id) {
        return subjectRepository.findById(id);
    }

    public Subject create(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Subject update(Long id, Subject subject) {
        var existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                        "id", "Subject not found", id.toString()));

        existingSubject.setName(subject.getName());
        existingSubject.setDescription(subject.getDescription());
        existingSubject.setModule(subject.getModule());
       // existingSubject.setRooms(subject.getRooms());

        return subjectRepository.save(existingSubject);
    }

    public Subject delete(Long id) {
        var existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                        "id", "Subject not found", id.toString()));

        existingSubject.setDeleted(true);
        return subjectRepository.save(existingSubject);
    }

}
