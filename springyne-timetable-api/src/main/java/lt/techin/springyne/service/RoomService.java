package lt.techin.springyne.service;

import lt.techin.springyne.dto.RoomDto;
import lt.techin.springyne.model.Room;
import lt.techin.springyne.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room addRoom(Room room){
        return roomRepository.save(room);
    }

    public Optional<Room> viewRoom(Long id) {
        return roomRepository.findById(id);
    }
//    public Room viewRoom(Long id){
//        return roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
//    }

    public Room editRoom(Long id, Room room) {
        room.setId(id);
        return roomRepository.save(room);
    }

//    public void editRoom(Room room){
//        roomRepository.save(room);
//    }

//    public boolean deleteById(Long id) {
//        if (roomRepository.existsById(id)) {
//            roomRepository.deleteById(id);
//            return true;
//        }
//        return false;
//    }

}
