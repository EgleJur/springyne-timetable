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
import org.springframework.transaction.annotation.Transactional;

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
            .withIgnorePaths("id", "module","deleted", "last_updated");

    private Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Subject does not exist", "id",
                        "Subject not found", String.valueOf(id)));
    }

    private Module getModuleById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Module does not exist", "id",
                        "Module not found", String.valueOf(id)));
    }

    private Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Room does not exist", "id",
                        "Room not found", String.valueOf(id)));
    }

    private void checkSubjectNameEmpty(String name) {
        if (name == null || name.equals("")) {
            throw new ScheduleValidationException("Subject name cannot be empty", "name",
                    "Name is empty", name);
        }
    }

    private void checkSubjectNameUnique(String name) {
       if (existsByName(name)) {
            throw new ScheduleValidationException("Subject name must be unique",
                    "name", "Name already exists", name);
        }
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Page<Subject> searchByNamePaged(String name, String moduleName, int page, int pageSize) {

        Subject subject = new Subject();
        if (name != null) {
            subject.setName(name);
        }

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("deleted").and(Sort.by("name")));
        if(moduleName == null || moduleName.isEmpty() || moduleName.isBlank()) {
            Example<Subject> subjectExample = Example.of(subject, SEARCH_CONTAINS_NAME);
            return subjectRepository.findAll(subjectExample, pageable);
        }
        if(name == null || name.isEmpty()|| name.isBlank() || name.equals("")) {

            return subjectRepository.findAllByModuleNameIgnoreCaseContaining(moduleName, pageable);
        }
        return  subjectRepository.findAllByNameIgnoreCaseContainingOrModuleNameIgnoreCaseContaining(name,moduleName, pageable);

    }

    public Page<Subject> getByModule(String name, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);
        return subjectRepository.findAllByModuleNameIgnoreCaseContaining(name, pageable);
    }

    public Optional<Subject> getById(Long id) {
        return subjectRepository.findById(id);
    }

    public Subject createSubject(Long moduleId, Long roomId, Subject subject) {

        checkSubjectNameEmpty(subject.getName());
        checkSubjectNameUnique(subject.getName());
        if (moduleId != null) {
            subject.setModule(getModuleById(moduleId));
        }
        //Subject createdSubject = subjectRepository.save(subject);
        if (roomId != null) {
            Room roomToAdd = getRoomById(roomId);
            subject.getRooms().add(roomToAdd);
            //subjectRepository.insertSubjectAndRoom(createdSubject.getId(), roomId);
        }

        return subjectRepository.save(subject);

    }

    public void addRoomFromSubject(Long subjectId, Long roomId) {
        if (subjectId == null) {
            throw new ScheduleValidationException("Subject id cannot be empty", "id",
                    "Id is empty", String.valueOf(subjectId));
        }
        Subject updatedSubject = getSubjectById(subjectId);
        if (roomId != null) {
            Room newRoom = getRoomById(roomId);
            Set<Room> existingRoom = updatedSubject.getRooms();
            if (!existingRoom.contains(newRoom)) {
                subjectRepository.insertSubjectAndRoom(subjectId, roomId);
            }
        }
    }

    public Subject edit(Long subjectId, Subject subject, Long moduleId, Long roomId) {

        checkSubjectNameEmpty(subject.getName());
        Subject updatedSubject = getSubjectById(subjectId);
        if(!updatedSubject.getName().equals(subject.getName())) {
            checkSubjectNameUnique(subject.getName());
        }
        updatedSubject.setName(subject.getName());
        updatedSubject.setDescription(subject.getDescription());

        if (moduleId != null) {
            Module moduleToAdd = getModuleById(moduleId);
            updatedSubject.setModule(moduleToAdd);
        }

        if (roomId != null) {
            Room newRoom = getRoomById(roomId);
            updatedSubject.getRooms().add(newRoom);
//            Set<Room> existingRoom = updatedSubject.getRooms();
//            if(!existingRoom.contains(newRoom)){
//                subjectRepository.insertSubjectAndRoom(id, roomId);
//            }
        }

        return subjectRepository.save(updatedSubject);
    }
    @Transactional
    public void deleteRoomFromSubject(Long subjectId, Long roomId) {

        Room roomToRemove = getRoomById(roomId);

        Subject getSubject = getSubjectById(subjectId);
        getSubject.getRooms().remove(roomToRemove);
//        subjectRepository.deleteRoomFromSubject(subjectId, roomId);

    }

    public boolean existsByName(String name) {
        return subjectRepository.existsByNameIgnoreCase(name);
    }

    public Subject createSubjectDto(Subject subject) {

        checkSubjectNameEmpty(subject.getName());
        checkSubjectNameUnique(subject.getName());
        return subjectRepository.save(subject);
    }


    public Subject delete(Long subjectId) {

        Subject existingSubject = getSubjectById(subjectId);
        if (!existingSubject.isDeleted()) {
            existingSubject.setDeleted(true);
            return subjectRepository.save(existingSubject);
        } else {
            return existingSubject;
        }

    }


    public Subject restore(Long id) {
        var existingSubject = getSubjectById(id);

        if (existingSubject.isDeleted()) {
            existingSubject.setDeleted(false);
            return subjectRepository.save(existingSubject);
        } else {
            return existingSubject;
        }
    }


    public Subject addModuleToSubject(Long subjectId, Long moduleId) {
        Module moduleToAdd = getModuleById(moduleId);
        Subject updatedSubject = getSubjectById(subjectId);
        updatedSubject.setModule(moduleToAdd);
        return subjectRepository.save(updatedSubject);
    }

}