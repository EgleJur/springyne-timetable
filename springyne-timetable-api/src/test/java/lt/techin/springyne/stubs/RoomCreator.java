package lt.techin.springyne.stubs;

import lt.techin.springyne.model.Room;

import java.util.HashSet;
import java.util.Set;

public class RoomCreator {
    public static Set<Room> createRoom(){
        Room room = new Room();
        room.setId(5L);
        Set<Room> rooms = new HashSet<>();

        rooms.add(room);


        return  rooms;
    }
}
