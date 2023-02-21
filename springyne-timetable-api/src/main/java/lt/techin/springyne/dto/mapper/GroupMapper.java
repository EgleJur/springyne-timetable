package lt.techin.springyne.dto.mapper;

import lt.techin.springyne.dto.GroupDto;
import lt.techin.springyne.model.Group;

public class GroupMapper {

    public static Group toGroup(GroupDto groupDto) {

        Group group = new Group();

        group.setName(groupDto.getName());
        group.setYear(groupDto.getYear());

        return group;
    }

    public static GroupDto toGroupDto(Group group) {

        GroupDto groupDto = new GroupDto();


        groupDto.setName(group.getName());
        groupDto.setYear(group.getYear());

        return groupDto;
    }


}