package lt.techin.springyne.controller;

import lt.techin.springyne.model.Group;
import lt.techin.springyne.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

//    @GetMapping("/byModule/search")
//
//    public Page<Group> getByModule(@RequestParam String name, @RequestParam int page,
//                                     @RequestParam int pageSize) {
//        return groupService.getByModule(name, page, pageSize);
//    }
//
//    @GetMapping("/{groupId}")
//    public Optional<Group> getGroup(@PathVariable Long groupId) {
//        return groupService.getById(groupId);
//    }

//    @PostMapping
//    public ResponseEntity<GroupDto> createGroupDto(@RequestBody GroupDto groupDto) {
//        var createdGroup = groupService.createGroupDto(toGroup(groupDto));
//
//        return ok(toGroupDto(createdGroup));
//    }
//    @PostMapping(value = "/createGroup")
//    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupDto groupDto,
//                                                 //   @RequestParam Long moduleId,
//                                                    @RequestParam(required = false) Long roomId) {
//        var createdGroup = groupService.createGroup( roomId, toGroup(groupDto));
//        return ok(toGroupDto(createdGroup));
//    }


//    @PatchMapping("/edit/{groupId}")
//    public ResponseEntity<Group> editGroup(@PathVariable Long groupId,
//                                               @RequestBody GroupDto groupDto,
//                                             //  @RequestParam(required = false) Long moduleId,
//                                               @RequestParam(required = false) Long shiftId) {
//        var updatedGroup = groupService.edit(groupId, toGroup(groupDto), shiftId);
//
//        return ok(updatedGroup);
//    }


//    @PatchMapping("/delete/groupId}")
//    public ResponseEntity<Group> deleteGroup(@PathVariable Long groupId) {
//
//        var updatedGroup = groupService.delete(groupId);
//        return ok(updatedGroup);
//
//    }
//
//    @PatchMapping("/restore/{groupId}")
//    public ResponseEntity<Group> restoreGroup(@PathVariable Long groupId) {
//
//        var restoredGroup = groupService.restore(groupId);
//        return ok(restoredGroup);
//
//    }

//    @PatchMapping("/{groupId}/addModule/{moduleId}")
//    public ResponseEntity<Group> addModuleToGroup(@PathVariable Long groupId, @PathVariable Long moduleId) {
//        return ResponseEntity.ok(groupService.addModuleToGroup(groupId, moduleId));
//    }

//    @PatchMapping("/{subjectId}/deleteShift/{shiftId}")
//    public void deleteShiftFromGroup(@PathVariable Long groupId, @PathVariable Long shiftId) {
//        groupService.deleteShiftFromGroup(groupId, shiftId);
//    }
//    @PatchMapping("/{subjectId}/addShift/{shiftId}")
//    public void addShiftFromGroup(@PathVariable Long groupId, @PathVariable Long shiftId) {
//        groupService.addShiftFromGroup(groupId, shiftId);
//    }


}