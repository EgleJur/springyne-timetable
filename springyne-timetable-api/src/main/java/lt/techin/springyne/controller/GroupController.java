package lt.techin.springyne.controller;

import lt.techin.springyne.dto.GroupDto;
import lt.techin.springyne.model.Group;
import lt.techin.springyne.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static lt.techin.springyne.dto.mapper.GroupMapper.toGroup;

import static lt.techin.springyne.dto.mapper.GroupMapper.toGroupDto;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/groups")

public class GroupController {

    @Autowired
    GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<Group> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/search")
    public Page<Group> searchByNamePaged(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String programName,
                                         @RequestParam(required = false) String groupYear,
                                           @RequestParam int page,
                                           @RequestParam int pageSize) {
        return groupService.searchByNamePaged(name,programName, groupYear, page, pageSize);
    }

    @GetMapping("/{groupId}")
    public Optional<Group> viewGroup(@PathVariable Long groupId) {
        return groupService.getById(groupId);
    }

    @PostMapping(value = "/createGroup")
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupDto groupDto,
                                                @RequestParam Long programId,
                                                @RequestParam Long shiftId) {
        var createdGroup = groupService.createGroup(programId, shiftId, toGroup(groupDto));
        return ok(toGroupDto(createdGroup));
    }

    @PatchMapping("/edit/{groupId}")
    public ResponseEntity<Group> editGroup(@PathVariable Long groupId,
                                           @RequestBody GroupDto groupDto,
                                           @RequestParam(required = false) Long programId,
                                           @RequestParam(required = false) Long shiftId)
    {
        var updatedGroup = groupService.edit(groupId, toGroup(groupDto), shiftId, programId);

        return ok(updatedGroup);
    }

    @PatchMapping("/delete/{groupId}")
    public ResponseEntity<Group> deleteGroup(@PathVariable Long groupId) {

        var updatedGroup = groupService.delete(groupId);
        return ok(updatedGroup);
    }

    @PatchMapping("/restore/{groupId}")
    public ResponseEntity<Group> restoreGroup(@PathVariable Long groupId) {

        var restoredGroup = groupService.restore(groupId);
        return ok(restoredGroup);

    }

}