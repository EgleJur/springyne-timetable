package lt.techin.springyne.room;

public class RoomMapper {

    public static Room toRoom(RoomDto roomDto) {

        Room room = new Room();
        room.setName(roomDto.getName());
        room.setBuilding(roomDto.getBuilding());
        room.setDescription(roomDto.getDescription());

        return room;
    }

    public static RoomDto toRoomDto(Room room){

        RoomDto roomDto = new RoomDto();
        roomDto.setName(room.getName());
        roomDto.setBuilding(room.getBuilding());
        roomDto.setDescription(room.getDescription());

        return roomDto;
    }
}
