package lt.techin.springyne.room;

import lt.techin.springyne.validationUnits.RoomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static lt.techin.springyne.validationUnits.ValidationUtilsNotNull.isValidByName;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    RoomUtils roomUtils;

    public RoomService(RoomRepository roomRepository) {

        this.roomRepository = roomRepository;
        roomUtils = new RoomUtils(roomRepository);
    }

    public List<Room> getAllRooms() {

        return roomRepository.findAllByOrderByDeletedAscIdAsc();
    }

    public Room addRoom(Room room) {

        return roomRepository.save(room);
    }

    public Page<Room> searchByName(String name, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page,pageSize, Sort.by("deleted"));
        return roomRepository.findAllByNameIgnoreCaseContaining(name, pageable);
    }

    public Page<Room> searchByBuilding(String building, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page,pageSize, Sort.by("deleted"));
        return roomRepository.findAllByBuildingIgnoreCaseContaining(building, pageable);
    }

    public Page<Room> searchByRoomAndBuildingPaged(String name, String building, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("deleted").and(Sort.by("name")));
        if(building == null || building.isEmpty() || building.isBlank()) {
            if(name == null || name.isEmpty()|| name.isBlank() || name.equals("")) {
                return roomRepository.findAll(pageable);
            }
            return roomRepository.findAllByNameIgnoreCaseContaining(name, pageable);
        } else if (name == null || name.isEmpty()|| name.isBlank() || name.equals("")) {
            return roomRepository.findAllByBuildingIgnoreCaseContaining(building, pageable);
        }

        return  roomRepository.findAllByNameIgnoreCaseContainingAndBuildingIgnoreCaseContaining(name, building, pageable);
    }

    public Optional<Room> viewRoom(Long id) {

        return roomRepository.findById(id);
    }

    public Room editRoom(Long id, Room room) {

        isValidByName(room.getName());
        Room existingRoom = roomUtils.getRoomById(id);

        if (!existingRoom.getName().equals(room.getName())) {
            roomUtils.checkRoomNameUnique(room.getName());
            existingRoom.setName(room.getName());
        }

        existingRoom.setBuilding(room.getBuilding());
        existingRoom.setDescription(room.getDescription());

        return roomRepository.save(existingRoom);
    }

    public Room delete(Long id) {

        Room existingRoom = roomUtils.getRoomById(id);

        existingRoom.setDeleted(true);
        return roomRepository.save(existingRoom);
    }

    public Room restore(Long id) {

       Room existingRoom = roomUtils.getRoomById(id);

        existingRoom.setDeleted(false);
        return roomRepository.save(existingRoom);
    }
}
