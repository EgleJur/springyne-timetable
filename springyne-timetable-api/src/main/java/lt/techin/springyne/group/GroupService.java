package lt.techin.springyne.group;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.program.Program;
import lt.techin.springyne.shift.Shift;
import lt.techin.springyne.program.ProgramRepository;
import lt.techin.springyne.shift.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    ProgramRepository programRepository;


    public GroupService(GroupRepository groupRepository, ShiftRepository shiftRepository, ProgramRepository programRepository) {
        this.groupRepository = groupRepository;
        this.programRepository = programRepository;
        this.shiftRepository = shiftRepository;

    }

    private Group getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Group does not exist", "id",
                        "Group not found", String.valueOf(id)));
    }

    private Shift getShiftById(Long id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Shift does not exist", "id",
                        "Shift not found", String.valueOf(id)));
    }

    private Program getProgramById(Long id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Program does not exist", "id",
                        "Program not found", String.valueOf(id)));
    }

    private void checkGroupNameEmpty(String name) {
        if (name == null || name.equals("")) {
            throw new ScheduleValidationException("Group name cannot be empty", "name",
                    "Name is empty", name);
        }
    }

    private void checkGroupNameUnique(String name) {
        if (existsByName(name)) {
            throw new ScheduleValidationException("Group name must be unique",
                    "name", "Name already exists", name);
        }
    }

    public boolean existsByName(String name) {
        return groupRepository.existsByNameIgnoreCase(name);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Page<Group> searchByNamePaged(String name, String programName, String groupYear, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("deleted").and(Sort.by("name")));

        if (programName == null || programName.equals("")) {
            if (groupYear == null || groupYear.equals("")) {
                if (name == null || name.equals("")) {
                    return groupRepository.findAll(pageable);
                }
                return groupRepository.findAllByNameIgnoreCaseContaining(name, pageable);
            } else if (name == null || name.equals("")) {
                return groupRepository.findAllByGroupYearIgnoreCaseContaining(groupYear, pageable);
            }
            return groupRepository.findAllByNameIgnoreCaseContainingOrGroupYearIgnoreCaseContaining(name, groupYear, pageable);

        } else if (groupYear == null || groupYear.equals("")) {
            if (name == null || name.equals("")) {

                return groupRepository.findAllByProgramNameIgnoreCaseContaining(programName, pageable);
            }
            return groupRepository.findAllByNameIgnoreCaseContainingOrProgramNameIgnoreCaseContaining(name, programName, pageable);
        } else if (name == null || name.equals("")) {
            return groupRepository.findAllByProgramNameIgnoreCaseContainingOrGroupYearIgnoreCaseContaining(programName, groupYear, pageable);
        }
        return groupRepository.findAllByNameIgnoreCaseContainingOrProgramNameIgnoreCaseContainingOrGroupYearIgnoreCaseContaining(name, programName, groupYear, pageable);
    }

    public Optional<Group> getById(Long id) {
        return groupRepository.findById(id);
    }

    public Group addGroup(Long programId, Long shiftId, Group group) {

        checkGroupNameEmpty(group.getName());
        String groupYear = group.getGroupYear();

        if (groupYear == null || groupYear.equals("")) {
            throw new ScheduleValidationException("Group year cannot be empty", "year",
                    "Year is empty", groupYear);
        }

        checkGroupNameUnique(group.getName());
        if (programId == null || programId.equals("")) {
            throw new ScheduleValidationException("Program id cannot be empty", "id",
                    "Id is empty", String.valueOf(programId));
        }
        if (shiftId == null || shiftId.equals("")) {
            throw new ScheduleValidationException("Shift id cannot be empty", "id",
                    "Id is empty", String.valueOf(shiftId));
        }
        group.setProgram(getProgramById(programId));
        group.setShift(getShiftById(shiftId));

        return groupRepository.save(group);
    }

    public Group edit(Long groupId, Group group, Long shiftId, Long programId) {

        checkGroupNameEmpty(group.getName());

        Group updatedGroup = getGroupById(groupId);
        if (!updatedGroup.getName().equals(group.getName())) {
            checkGroupNameUnique(group.getName());
            updatedGroup.setName(group.getName());
        }
        if (group.getGroupYear() == null || group.getGroupYear().equals("")) {
            throw new ScheduleValidationException("Group year cannot be empty", "year",
                    "Year is empty", group.getGroupYear());
        }
        if (shiftId == null) {
            throw new ScheduleValidationException("Shift id cannot be empty", "id",
                    "Id is empty", String.valueOf(shiftId));
        }

        if (programId == null) {
            throw new ScheduleValidationException("Program id cannot be empty", "id",
                    "Id is empty", String.valueOf(programId));
        }

        Program newProgram = getProgramById(programId);
        updatedGroup.setProgram(newProgram);
        Shift shiftToAdd = getShiftById(shiftId);
        updatedGroup.setShift(shiftToAdd);
        updatedGroup.setGroupYear(group.getGroupYear());
        updatedGroup.setStudents(group.getStudents());


        return groupRepository.save(updatedGroup);
    }

    public Group delete(Long groupId) {

        Group existingGroup = getGroupById(groupId);
        if (!existingGroup.isDeleted()) {
            existingGroup.setDeleted(true);
            return groupRepository.save(existingGroup);
        } else {
            return existingGroup;
        }
    }

    public Group restore(Long id) {
        var existingGroup = getGroupById(id);

        if (existingGroup.isDeleted()) {
            existingGroup.setDeleted(false);
            return groupRepository.save(existingGroup);
        } else {
            return existingGroup;
        }
    }
}
