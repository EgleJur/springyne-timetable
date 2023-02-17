package lt.techin.springyne.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lt.techin.springyne.dto.RoomDto;
import lt.techin.springyne.model.Room;
import lt.techin.springyne.service.RoomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

//    @Test
//    void getAllRoomsContainsCorrectDtos() throws Exception {
//
//        RoomDto testRoomDto1 = new RoomDto("R1", "Test name1", "Test");
//        RoomDto testRoomDto2 = new RoomDto("R2", "Test name2", "Test");
//        RoomDto testRoomDto3 = new RoomDto("R3", "Test name2", "Test");
//
//        List<RoomDto> expectedList = new ArrayList<>();
//        expectedList.add(testRoomDto1);
//        expectedList.add(testRoomDto2);
//        expectedList.add(testRoomDto3);
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms")
//        ).andExpect(status().isOk()).andReturn();
//
//        List<RoomDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<RoomDto>>() {
//        });
//
//        assertTrue(resultList.containsAll(expectedList));
//    }

    @Test
    public void testRoomDto() {
        RoomDto room = new RoomDto("R1", "Test name1", "");

        assertEquals("R1", room.getName());
        assertEquals("Test name1", room.getBuilding());
        assertEquals("", room.getDescription());

        room.setName("R2");
        room.setBuilding("Test name2");
        room.setDescription("Test");

        assertEquals("R2", room.getName());
        assertEquals("Test name2", room.getBuilding());
        assertEquals("Test", room.getDescription());
    }

    @Test
    void addRoomThrowsExceptionWithNullOrEmptyValues() throws Exception {
        RoomDto testRoomDto4 = new RoomDto("", "Test name4", "Test");
        RoomDto testRoomDto5 = new RoomDto(null, "Test name5", "Test");
        RoomDto testRoomDto6 = new RoomDto(null, null, null);


        String message = "Null or empty values should return bad request status";

        assertEquals(400,performRoomPostBadRequest(testRoomDto4).getResponse().getStatus(), message);
        assertEquals(400,performRoomPostBadRequest(testRoomDto5).getResponse().getStatus(), message);
        assertEquals(400,performRoomPostBadRequest(testRoomDto6).getResponse().getStatus(), message);
    }

    @Test
    void deleteRoomSetsDeletedPropertyToTrue() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/rooms/delete/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Room resultRoom = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Room>() {});
        assertTrue(resultRoom.isDeleted());
    }

    @Test
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
        Assertions.assertEquals(result.getName(), "R1","Get room by Id should return room with correct name");
    }

//    @Test
//    void editRoomThrowsExceptionWithEmptyValues() throws Exception {
//        RoomDto testRoomDto1 = new RoomDto("", "");
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/room/edit/1").contentType(MediaType.APPLICATION_JSON).
//                content(objectMapper.writeValueAsString(testRoomDto1))).andReturn();
//
//        assertEquals(400, mvcResult.getResponse().getStatus(),"Empty value name and building should return bad request status");
//    }


    @Test
    void editRoomDtoAllowsSavingWithCorrectValues() {
        String testName = "R1";
        String testBuilding = "Test name1";
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
        RoomDto testRoomDto1 = new RoomDto("R1", "Test name1", "Test");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms/1")
        ).andExpect(status().isOk()).andReturn();
        RoomDto resultRoomDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<RoomDto>() {
        });
        assertEquals(resultRoomDto, testRoomDto1);
    }

    @Test
    public void getAllRoomsTest(){
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        when(roomService.getAllRooms()).thenReturn(rooms);
        assertEquals(roomController.getAllRooms().size(), rooms.size());
    }

}