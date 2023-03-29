package lt.techin.springyne.validationUnits;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.group.Group;
import lt.techin.springyne.group.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class GroupUtils {

    @Autowired
    private final GroupRepository groupRepository;

    public GroupUtils(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Group does not exist", "id",
                        "Group not found", String.valueOf(id)));
    }

    public boolean existsByName(String name) {
        return groupRepository.existsByNameIgnoreCase(name);
    }

    public void checkGroupNameUnique(String name) {
        if (existsByName(name)) {
            throw new ScheduleValidationException("Group name must be unique",
                    "name", "Name already exists", name);
        }
    }
}
