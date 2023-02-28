package lt.techin.springyne.room;

import lt.techin.springyne.exception.ScheduleValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    private static final ExampleMatcher SEARCH_CONTAINS_NAME = ExampleMatcher.matchingAny()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withIgnorePaths("id", "deleted","building", "lastModifiedDate");

    private static final ExampleMatcher SEARCH_CONTAINS_BUILDING = ExampleMatcher.matchingAny()
            .withMatcher("building", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withIgnorePaths("id", "deleted", "name", "lastModifiedDate");

    public RoomService(RoomRepository roomRepository) {

        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {

        return roomRepository.findAllByOrderByDeletedAscIdAsc();
    }

    public Room addRoom(Room room) {

        if (existsByName(room.getName())) {
        throw new ScheduleValidationException("Room name must be unique", "name", "Name already exists", room.getName());
    }
    return roomRepository.save(room);
    }

    public boolean existsByName(String name) {

        return roomRepository.existsByNameIgnoreCase(name);
    }

    public Page<Room> searchByName(String name, int page, int pageSize) {

        Room room = new Room();
        if(name != null) {
            room.setName(name);
        }
        Example<Room> roomExample = Example.of(room, SEARCH_CONTAINS_NAME);
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by("deleted"));
        return roomRepository.findAll(roomExample,pageable);
    }

    public boolean existsByBuilding(String building) {

        return roomRepository.existsByBuildingIgnoreCase(building);
    }

    public Page<Room> searchByBuilding(String building, int page, int pageSize) {

        Room room = new Room();
        if(building != null) {
            room.setBuilding(building);
        }
        Example<Room> roomExample = Example.of(room, SEARCH_CONTAINS_BUILDING);
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by("deleted"));
        return roomRepository.findAll(roomExample,pageable);
    }

    public Optional<Room> viewRoom(Long id) {

        return roomRepository.findById(id);
    }

    public Room editRoom(Long id, Room room) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Room does not exist",
                        "id", "Room not found", id.toString()));

        existingRoom.setName(room.getName());
        existingRoom.setBuilding(room.getBuilding());
        existingRoom.setDescription(room.getDescription());

        return roomRepository.save(existingRoom);
    }

    public Room delete(Long id) {
        var existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Room does not exist",
                        "id", "Room not found", id.toString()));

        existingRoom.setDeleted(true);
        return roomRepository.save(existingRoom);
    }

    public Room restore(Long id) {
        var existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new ScheduleValidationException("Room does not exist",
                        "id", "Room not found", id.toString()));

        existingRoom.setDeleted(false);
        return roomRepository.save(existingRoom);
    }

    public Page<Room> searchByRoomAndBuildingPaged(String name, String building, int page, int pageSize) {

            Room room = new Room();
            if (name != null) {
                room.setName(name);
            }

            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("deleted").and(Sort.by("name")));
            if(building == null || building.isEmpty() || building.isBlank()) {
                Example<Room> roomExample = Example.of(room, SEARCH_CONTAINS_NAME);
                return roomRepository.findAll(roomExample, pageable);
            }
            if(name == null || name.isEmpty()|| name.isBlank() || name.equals("")) {

                return roomRepository.findAllByBuildingIgnoreCaseContaining(building, pageable);
            }
            return  roomRepository.findAllByNameIgnoreCaseContainingOrBuildingIgnoreCaseContaining(name,building, pageable);

        }
}