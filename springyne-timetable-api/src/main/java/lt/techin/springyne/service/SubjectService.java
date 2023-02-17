package lt.techin.springyne.service;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.model.Module;
import lt.techin.springyne.model.Room;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.repository.ModuleRepository;
import lt.techin.springyne.repository.RoomRepository;
import lt.techin.springyne.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    RoomRepository roomRepository;


    public SubjectService(SubjectRepository subjectRepository, ModuleRepository moduleRepository, RoomRepository roomRepository) {
        this.subjectRepository = subjectRepository;
        this.moduleRepository = moduleRepository;
        this.roomRepository = roomRepository;

    }

    private static final ExampleMatcher SEARCH_CONTAINS_NAME = ExampleMatcher.matchingAny()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withIgnorePaths("id", "deleted", "last_updated");


    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Page<Subject> searchByNamePaged(String name, int page, int pageSize) {

        Subject subject = new Subject();
        if (name != null) {
            subject.setName(name);
        }
        Example<Subject> subjectExample = Example.of(subject, SEARCH_CONTAINS_NAME);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("deleted").and(Sort.by("name")));
        return subjectRepository.findAll(subjectExample, pageable);
    }

    public Page<Subject> getByModule(String name, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return subjectRepository.findByModuleName(name, pageable);
    }

    public Optional<Subject> getById(Long id) {
        return subjectRepository.findById(id);
    }

    public Subject createSubject(Long moduleId, Long roomId, Subject subject) {
        if (existsByName(subject.getName())) {
            throw new ScheduleValidationException("Subject name must be unique", "name", "Name already exists", subject.getName());
        }
        if (moduleId != null) {
            Module moduleById = moduleRepository.findById(moduleId)
                    .orElseThrow(() -> new ScheduleValidationException("Module does not exist", "id",
                            "Module not found", String.valueOf(moduleId)));


            subject.setModule(moduleById);
        }
        Subject createdSubject = subjectRepository.save(subject);
        if (roomId != null) {
            roomRepository.findById(roomId)
                    .orElseThrow(() -> new ScheduleValidationException("Room does not exist", "id",
                            "Room not found", String.valueOf(roomId)));

            subjectRepository.insertSubjectAndRoom(createdSubject.getId(), roomId);
        }

        return createdSubject;

    }

    public void addRoomFromSubject(Long subjectId, Long roomId) {
        Subject updatedSubject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist", "id",
                        "Subject not found", String.valueOf(subjectId)));

        if (roomId != null) {
           Room newRoom = roomRepository.findById(roomId)
                    .orElseThrow(() -> new ScheduleValidationException("Room does not exist", "id",
                            "Room not found", String.valueOf(roomId)));

            Set<Room> existingRoom = updatedSubject.getRooms();
            if(!existingRoom.contains(newRoom)){
                subjectRepository.insertSubjectAndRoom(subjectId, roomId);
            }
        }
    }

    public Subject edit(Long id, Subject subject, Long roomId, Long moduleId) {
        Subject updatedSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist", "id",
                        "Subject not found", String.valueOf(id)));

        updatedSubject.setName(subject.getName());
        updatedSubject.setDescription(subject.getDescription());

        if (moduleId != null){
            Module moduleToAdd = moduleRepository.findById(moduleId)
                    .orElseThrow(() -> new ScheduleValidationException("Module does not exist", "id",
                            "Module not found", String.valueOf(moduleId)));

            updatedSubject.setModule(moduleToAdd);
        }

//        if (roomId != null) {
//            var newRoom = roomRepository.findById(roomId)
//                    .orElseThrow(() -> new ScheduleValidationException("Room does not exist", "id",
//                            "Room not found", String.valueOf(roomId)));
//            Set<Room> existingRoom = updatedSubject.getRooms();
//            if(!existingRoom.contains(newRoom)){
//                subjectRepository.insertSubjectAndRoom(id, roomId);
//            }
//        }

        return subjectRepository.save(updatedSubject);
    }
    public void deleteRoomFromSubject(Long subjectId, Long roomId){
        Room roomToRemove = roomRepository.findById(roomId)
                .orElseThrow(() -> new ScheduleValidationException("Room does not exist", "id",
                        "Room not found", String.valueOf(roomId)));
        Subject updatedSubject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist", "id",
                        "Subject not found", String.valueOf(subjectId)));
        subjectRepository.deleteRoomFromSubject(subjectId, roomId);

    }

    public boolean existsByName(String name) {
        return subjectRepository.existsByNameIgnoreCase(name);
    }

    public Subject createSubjectDto(Subject subject) {
        if (existsByName(subject.getName())) {
            throw new ScheduleValidationException("Subject name must be unique", "name", "Name already exists", subject.getName());
        }
        return subjectRepository.save(subject);
    }


    public Subject delete(Long id) {
        var existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                        "id", "Subject not found", id.toString()));
        if (!existingSubject.isDeleted()) {
            existingSubject.setDeleted(true);
            return subjectRepository.save(existingSubject);
        } else {
            return existingSubject;
        }

    }


    public Subject restore(Long id) {
        var existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist",
                        "id", "Subject not found", id.toString()));

        if (existingSubject.isDeleted()) {
            existingSubject.setDeleted(false);
            return subjectRepository.save(existingSubject);
        } else {
            return existingSubject;
        }
    }


    public Subject addModuleToSubject(Long subjectId, Long moduleId) {
        Module moduleToAdd = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ScheduleValidationException("Module does not exist", "id",
                        "Module not found", String.valueOf(moduleId)));
        Subject updatedSubject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist", "id",
                        "Subject not found", String.valueOf(subjectId)));
        updatedSubject.setModule(moduleToAdd);
        return subjectRepository.save(updatedSubject);
    }

}