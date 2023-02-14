package lt.techin.springyne.dto.mapper;

import lt.techin.springyne.dto.RoomDto;
import lt.techin.springyne.model.Room;

public class RoomMapper {

    public static Room toRoom(RoomDto roomDto) {

        Room room = new Room();
        room.setName(roomDto.getName());
        room.setBuilding(roomDto.getBuilding());
        room.setDescription(roomDto.getDescription());
        room.setDeleted(roomDto.isDeleted());
        //data
        //deleted

        return room;
    }

    public static RoomDto toRoomDto(Room room){

        RoomDto roomDto = new RoomDto();
        roomDto.setName(room.getName());
        roomDto.setBuilding(room.getBuilding());
        roomDto.setDescription(room.getDescription());
        roomDto.setDeleted(room.isDeleted());

        return roomDto;
    }
}
