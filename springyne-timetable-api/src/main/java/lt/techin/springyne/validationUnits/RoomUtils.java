package lt.techin.springyne.validationUnits;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.room.Room;
import lt.techin.springyne.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class RoomUtils {

    @Autowired
    private final RoomRepository roomRepository;

    public RoomUtils(RoomRepository roomRepository) {

        this.roomRepository = roomRepository;
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Room does not exist", "id",
                        "Room not found", String.valueOf(id)));
    }

    public boolean existsByName(String name) {

        return roomRepository.existsByNameIgnoreCase(name);
    }

    public void checkRoomNameUnique(String name) {
        if (existsByName(name)) {
            throw new ScheduleValidationException("Room name must be unique", "name", "Name already exists", name);
        }
    }
}
