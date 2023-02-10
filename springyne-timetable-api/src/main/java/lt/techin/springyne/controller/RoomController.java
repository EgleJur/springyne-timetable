package lt.techin.springyne.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static lt.techin.springyne.dto.mapper.RoomMapper.toRoom;
import static lt.techin.springyne.dto.mapper.RoomMapper.toRoomDto;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    ObjectMapper objetMapper;


    public RoomController (RoomService roomService){

        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getAllRooms(){

        return roomService.getAllRooms();
    }

    @PostMapping
    public ResponseEntity<Object> addRoom(@Valid @RequestBody RoomDto roomDto) {
        if (roomService.existsByName(roomDto.getName())) {
            return ResponseEntity.badRequest().body("Toks kabinetas jau egzistuoja");
        }

        Room newRoom = roomService.addRoom(RoomMapper.toRoom(roomDto));
        return ResponseEntity.ok(RoomMapper.toRoomDto(newRoom));
    }

    @GetMapping("/searchByName")
    public List<Room> filterRoomsByNamePaged(@RequestParam(required = false) String name,
                                                 @RequestParam int page, @RequestParam int pageSize) {
        return roomService.searchByName(name,page,pageSize).stream()
                .collect(Collectors.toList());
    }

    @GetMapping("/searchByBuilding")
    public List<Room> filterRoomsByBuildingPaged(@RequestParam(required = false) String building,
                                             @RequestParam int page, @RequestParam int pageSize) {
        return roomService.searchByBuilding(building,page,pageSize).stream()
                .collect(Collectors.toList());
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

    @PatchMapping("/delete/{id}")
    public ResponseEntity<RoomDto> deleteRoom(@PathVariable Long id) {

        var updatedRoom = roomService.delete(id);
        return ok(toRoomDto(updatedRoom));

    }
    @PatchMapping("/restore/{id}")
    public ResponseEntity<RoomDto> restoreRoom(@PathVariable Long id) {

        var restoredRoom = roomService.restore(id);
        return ok(toRoomDto(restoredRoom));

    }
}
