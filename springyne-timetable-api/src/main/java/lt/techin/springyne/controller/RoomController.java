package lt.techin.springyne.controller;

import lt.techin.springyne.dto.RoomDto;
import lt.techin.springyne.dto.mapper.RoomMapper;
import lt.techin.springyne.model.Room;
import lt.techin.springyne.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static lt.techin.springyne.dto.mapper.RoomMapper.toRoom;
import static lt.techin.springyne.dto.mapper.RoomMapper.toRoomDto;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    @Autowired
    RoomService roomService;

    public RoomController (RoomService roomService){
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomDto> getAllRooms() {
        return roomService.getAllRooms().stream()
                .map(RoomMapper::toRoomDto)
                .collect(toList());
    }

    @PostMapping
    public ResponseEntity<RoomDto> addRoom(@Valid @RequestBody RoomDto roomDto) {
        Room newRoom = roomService.addRoom(toRoom(roomDto));
        return ResponseEntity.ok(toRoomDto(newRoom));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> viewRoom(@PathVariable Long id) {
        var roomOptional = roomService.viewRoom(id);

        var responseEntity = roomOptional
                .map(room -> ok(toRoomDto(room)))
                .orElseGet(() -> ResponseEntity.notFound().build());

        return responseEntity;
    }

//    @GetMapping("/{id}")
//    public Room viewRoom(@PathVariable Long id) {
//        return roomService.viewRoom(id);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDto> editRoom(@PathVariable Long id, @RequestBody RoomDto roomDto) {
        var editedRoom = roomService.editRoom(id, toRoom(roomDto));

        return ok(toRoomDto(editedRoom));
    }

//    @PutMapping("/{id}")
//    public void editRoom(@PathVariable Long id, @RequestBody Room room){
//        roomService.editRoom(room);
//
//}


//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
//        var roomDeleted = roomService.deleteById(id);
//
//        if (roomDeleted) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }


}
