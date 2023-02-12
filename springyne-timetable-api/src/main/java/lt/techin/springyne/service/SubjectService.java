package lt.techin.springyne.service;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.repository.ModuleRepository;
import lt.techin.springyne.repository.RoomRepository;
import lt.techin.springyne.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;
    ModuleRepository moduleRepository;
    RoomRepository roomRepository;


    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;

    }

    private static final ExampleMatcher SEARCH_CONTAINS_NAME = ExampleMatcher.matchingAny()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withIgnorePaths("id","deleted","last_updated");

//    private static final ExampleMatcher SEARCH_CONTAINS_MODULE = ExampleMatcher.matchingAny()
//            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
//            .withIgnorePaths("id", "deleted","last_updated");


    public Page<Subject> searchByNamePaged(String name, int page, int pageSize) {

        Subject subject = new Subject();
        if(name != null) {
            subject.setName(name);
        }
        Example<Subject> subjectExample = Example.of(subject, SEARCH_CONTAINS_NAME);
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by("deleted").and(Sort.by("name")));
        return subjectRepository.findAll(subjectExample, pageable);
    }
    public Page<Subject> searchByModulePaged(String name, int page, int pageSize) {

        Subject subject = new Subject();
        if(name != null) {
            subject.setName(name);
        }
        Example<Subject> subjectExample = Example.of(subject, SEARCH_CONTAINS_NAME);
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by("deleted").and(Sort.by("name")));
        return subjectRepository.findAll(subjectExample, pageable);
    }

    public Page<Subject> getByModule(String name, int page, int pageSize){
        Pageable pageable = PageRequest.of(page,pageSize);
        return subjectRepository.findByModuleName(name, pageable);
    }

    public Optional<Subject> getById(Long id) {
        return subjectRepository.findById(id);
    }
    public boolean existsByName(String name) {
        return subjectRepository.existsByNameIgnoreCase(name);
    }

    public Subject create(Subject subject) {
        if (existsByName(subject.getName())) {
            throw new ScheduleValidationException("Subject name must be unique", "name", "Name already exists", subject.getName());
        }
        return subjectRepository.save(subject);
    }

    public Subject edit(Long id, Subject subject) {
        var existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                        "id", "Subject not found", id.toString()));

        existingSubject.setName(subject.getName());
        existingSubject.setDescription(subject.getDescription());

        return subjectRepository.save(existingSubject);
    }

    public Subject delete(Long id) {
        var existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                        "id", "Subject not found", id.toString()));

        existingSubject.setDeleted(true);
        return subjectRepository.save(existingSubject);
    }


    public Subject restore(Long id) {
        var existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                        "id", "Subject not found", id.toString()));

        existingSubject.setDeleted(false);
        return subjectRepository.save(existingSubject);
    }


//        if(moduleId!=null) {
//
//            var existingModule = moduleRepository.findById(moduleId)
//                    .orElseThrow(() -> new ScheduleValidationException("Module does not exist",
//                            "id", "Subject not found", moduleId.toString()));

        //}

//        if(roomId!=null){
//
//            var existingRoom = roomRepository.findById(roomId)
//                .orElseThrow(() -> new ScheduleValidationException("Room does not exist",
//                        "id", "Room not found", roomId.toString()));
//            existingSubject.addRoom(existingRoom);
//        }


//        var existingModule = moduleRepository.findById(moduleId)
//                .orElseThrow(() -> new ScheduleValidationException("Module does not exist",
//                        "id", "Module not found", id.toString()));
//        var existingRoom = roomRepository.findById(roomId)
//                .orElseThrow(() -> new ScheduleValidationException("Room does not exist",
//                        "id", "Room not found", id.toString()));

//        existingSubject.setName(subject.getName());
//        existingSubject.setDescription(subject.getDescription());
//         existingSubject.setModule(existingModule);
//         existingSubject.setRooms(existingRoom);
       // existingSubject.setModule(subject.getModule());
       // existingSubject.setRooms(subject.getRooms());







//    @Transactional
//    public void addTagToPost(Long postId, Long tagId) {
//        Subject post = subjectRepository.findModuleWithSubjectById(postId).orElseThrow();
//        Module tag = moduleRepository.findById(tagId).orElseThrow();
//        post.addModule(tag);
//    }

//        public Subject addRoomToAnimal(Long animalId, Long roomId) {
//        // - find animal
//        var existingAnimal = subjectRepository.findById(animalId)
//                .orElseThrow(() -> new ScheduleValidationException("Animal does not exist",
//                        "id", "Animal not found", animalId.toString()));
//
//        // - find room
//        var existingRoom = moduleRepository.findById(roomId)
//                .orElseThrow(() -> new ScheduleValidationException("Room does not exist",
//                        "id", "Room not found", roomId.toString()));
//
//        // - if OK - set
//        //existingAnimal.setModule(existingRoom);
//
//        return subjectRepository.save(existingAnimal);
//    }


}
