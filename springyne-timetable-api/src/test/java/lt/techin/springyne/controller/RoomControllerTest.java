package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.room.Room;
import lt.techin.springyne.room.RoomController;
import lt.techin.springyne.room.RoomDto;
import lt.techin.springyne.room.RoomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RoomControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testRoomDto() {
        RoomDto room = new RoomDto("101", "Lakūnų g. 3, LT-09108 Vilnius", "");

        assertEquals("101", room.getName());
        assertEquals("Lakūnų g. 3, LT-09108 Vilnius", room.getBuilding());
        assertEquals("", room.getDescription());

        room.setName("102");
        room.setBuilding("Lakūnų g. 3, LT-09108 Vilnius");
        room.setDescription("Akademija.IT");

        assertEquals("102", room.getName());
        assertEquals("Lakūnų g. 3, LT-09108 Vilnius", room.getBuilding());
        assertEquals("Akademija.IT", room.getDescription());
    }

    @Test
    void addRoomThrowsExceptionWithNullOrEmptyValues() throws Exception {
        RoomDto testRoomDto4 = new RoomDto("", "Lakūnų g. 3, LT-09108 Vilnius", "Akademija.IT");
        RoomDto testRoomDto5 = new RoomDto(null, "Lakūnų g. 3, LT-09108 Vilnius", "Akademija.IT");
        RoomDto testRoomDto6 = new RoomDto(null, null, null);


        String message = "Null or empty values should return bad request status";

        assertEquals(400,performRoomPostBadRequest(testRoomDto4).getResponse().getStatus(), message);
        assertEquals(400,performRoomPostBadRequest(testRoomDto5).getResponse().getStatus(), message);
        assertEquals(400,performRoomPostBadRequest(testRoomDto6).getResponse().getStatus(), message);
    }

    @Test
    @Order(1)
    void deleteRoomSetsDeletedPropertyToTrue() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/rooms/delete/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Room resultRoom = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Room>() {});
        assertTrue(resultRoom.isDeleted());
    }

    @Test
    @Order(2)
    @DependsOn("deleteRoomSetsDeletedPropertyToTrue")
    void restoreRoomsSetsDeletedPropertyToFalse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/rooms/restore/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Room resultRoom = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Room>() {});
        Assertions.assertFalse(resultRoom.isDeleted());
    }

    @Test
    void getRoomByIdReturnsCorrectDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms/1")
        ).andExpect(status().isOk()).andReturn();
        RoomDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<RoomDto>() {
        });
        Assertions.assertEquals(result.getName(), "101","Get room by Id should return room with correct name");
    }

    @Test
    void editRoomDtoAllowsSavingWithCorrectValues() {
        String testName = "101";
        String testBuilding = "Lakūnų g. 3, LT-09108 Vilnius";
        RoomDto testRoomDto = new RoomDto(testName, testBuilding);

        Assertions.assertEquals(testName, testRoomDto.getName());
        Assertions.assertEquals(testBuilding, testRoomDto.getBuilding());
    }

    @Test
    public MvcResult performRoomPostBadRequest(RoomDto roomDto) throws Exception {

        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/rooms").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(roomDto)))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @InjectMocks
    RoomController roomController;

    @Mock
    RoomService roomService;

    @Mock
    Room room;

    @Mock
    RoomDto roomDto;

    private static final long Id = 1;

    @Test
    public void viewRoomByIdTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms/1")
        ).andExpect(status().isOk()).andReturn();
        RoomDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<RoomDto>() {
        });
        Assertions.assertEquals(result.getName(), "101","Get room by Id should return room with correct name");
    }

    @Test
    public void getAllRoomsTest(){
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        when(roomService.getAllRooms()).thenReturn(rooms);
        assertEquals(roomController.getAllRooms().size(), rooms.size());
    }

}