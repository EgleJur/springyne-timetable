package lt.techin.springyne.group;

import lt.techin.springyne.program.Program;
import lt.techin.springyne.program.ProgramRepository;
import lt.techin.springyne.shift.Shift;
import lt.techin.springyne.shift.ShiftRepository;
import lt.techin.springyne.validationUnits.GroupUtils;
import lt.techin.springyne.validationUnits.ProgramUtils;
import lt.techin.springyne.validationUnits.ShiftUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static lt.techin.springyne.validationUnits.ValidationUtilsNotNull.*;


@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    ProgramRepository programRepository;

    GroupUtils groupUtils;

    ProgramUtils programUtils;

    ShiftUtils shiftUtils;


    public GroupService(GroupRepository groupRepository,
                        ShiftRepository shiftRepository,
                        ProgramRepository programRepository) {
        this.groupRepository = groupRepository;
        this.programRepository = programRepository;
        this.shiftRepository = shiftRepository;

        groupUtils = new GroupUtils(groupRepository);
        programUtils = new ProgramUtils(programRepository);
        shiftUtils = new ShiftUtils(shiftRepository);

    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Page<Group> searchByNamePaged(String name,
                                         String programName, String groupYear,
                                         int page, int pageSize) {

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
            return groupRepository.findAllByNameIgnoreCaseContainingAndGroupYearIgnoreCaseContaining(name, groupYear, pageable);

        } else if (groupYear == null || groupYear.equals("")) {
            if (name == null || name.equals("")) {

                return groupRepository.findAllByProgramNameIgnoreCaseContaining(programName, pageable);
            }
            return groupRepository.findAllByNameIgnoreCaseContainingAndProgramNameIgnoreCaseContaining(name, programName, pageable);
        } else if (name == null || name.equals("")) {
            return groupRepository.findAllByProgramNameIgnoreCaseContainingOrGroupYearIgnoreCaseContaining(programName, groupYear, pageable);
        }
        return groupRepository.findAllByNameIgnoreCaseContainingAndProgramNameIgnoreCaseContainingAndGroupYearIgnoreCaseContaining(name, programName, groupYear, pageable);
    }

    public Optional<Group> getById(Long id) {
        return groupRepository.findById(id);
    }

    public Group addGroup(Long programId, Long shiftId, Group group) {

        isValidByName(group.getName());
        isValidById(programId);
        isValidById(shiftId);

        isValidByGroupYear(group.getGroupYear());

        groupUtils.checkGroupNameUnique(group.getName());

        group.setProgram(programUtils.getProgramById(programId));
        group.setShift(shiftUtils.getShiftById(shiftId));

        return groupRepository.save(group);
    }

    public Group edit(Long groupId, Group group, Long shiftId, Long programId) {

        isValidByName(group.getName());
        isValidByGroupYear(group.getGroupYear());

        Group updatedGroup = groupUtils.getGroupById(groupId);

        if (!updatedGroup.getName().equals(group.getName())) {
            groupUtils.checkGroupNameUnique(group.getName());
            updatedGroup.setName(group.getName());
        }
        if (programId != null) {
            Program program = programUtils.getProgramById(programId);
            updatedGroup.setProgram(program);
        }
        if (shiftId != null) {
            Shift shift = shiftUtils.getShiftById(shiftId);
            updatedGroup.setShift(shift);
        }

        updatedGroup.setGroupYear(group.getGroupYear());
        updatedGroup.setStudents(group.getStudents());

        return groupRepository.save(updatedGroup);
    }

    public Group delete(Long groupId) {

        Group existingGroup = groupUtils.getGroupById(groupId);
        if (!existingGroup.isDeleted()) {
            existingGroup.setDeleted(true);
            return groupRepository.save(existingGroup);
        } else {
            return existingGroup;
        }
    }

    public Group restore(Long id) {
        var existingGroup = groupUtils.getGroupById(id);

        if (existingGroup.isDeleted()) {
            existingGroup.setDeleted(false);
            return groupRepository.save(existingGroup);
        } else {
            return existingGroup;
        }
    }
}
