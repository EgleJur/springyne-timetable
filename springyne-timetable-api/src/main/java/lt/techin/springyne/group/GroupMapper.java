package lt.techin.springyne.group;

public class GroupMapper {

    public static Group toGroup(GroupDto groupDto) {

        Group group = new Group();

        group.setName(groupDto.getName());
        group.setGroupYear(groupDto.getGroupYear());
        group.setStudents(groupDto.getStudents());

        return group;
    }

    public static GroupDto toGroupDto(Group group) {

        GroupDto groupDto = new GroupDto();

        groupDto.setName(group.getName());
        groupDto.setGroupYear(group.getGroupYear());
        groupDto.setStudents(group.getStudents());

        return groupDto;
    }


}