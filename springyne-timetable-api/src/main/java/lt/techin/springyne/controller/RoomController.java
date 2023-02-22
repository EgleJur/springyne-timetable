package lt.techin.springyne.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.dto.RoomDto;
import lt.techin.springyne.dto.mapper.RoomMapper;
import lt.techin.springyne.model.Room;
import lt.techin.springyne.model.Subject;
import lt.techin.springyne.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Room> addRoom(@Valid @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.addRoom(RoomMapper.toRoom(roomDto)));
    }

    @GetMapping("/searchByName")
    public Page<Room> filterRoomsByNamePaged(@RequestParam(required = false) String name,
                                             @RequestParam int page, @RequestParam int pageSize) {
        return roomService.searchByName(name,page,pageSize);
    }

    @GetMapping("/searchByBuilding")
    public Page<Room> filterRoomsByBuildingPaged(@RequestParam(required = false) String building,
                                             @RequestParam int page, @RequestParam int pageSize) {
        return roomService.searchByBuilding(building,page,pageSize);
    }

    @GetMapping("/search")
    public Page<Room> searchByRoomAndBuildingPaged(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String building,
                                           @RequestParam int page,
                                           @RequestParam int pageSize) {
        return roomService.searchByRoomAndBuildingPaged(name, building, page, pageSize);
    }

    @GetMapping("/{id}")
    public Optional<Room> viewRoom(@PathVariable Long id) {
        return roomService.viewRoom(id);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<Room> editRoom(@PathVariable Long id, @Valid @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.editRoom(id, RoomMapper.toRoom(roomDto)));
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<Room> deleteRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.delete(id));
    }

    @PatchMapping("/restore/{id}")
    public ResponseEntity<Room> restoreRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.restore(id));
    }
}
