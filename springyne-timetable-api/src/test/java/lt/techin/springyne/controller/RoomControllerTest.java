package lt.techin.springyne.controller;

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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllRoomsContainsCorrectDtos() throws Exception {

        RoomDto testRoomDto1 = new RoomDto("R1", "Test name1", "Test");
        RoomDto testRoomDto2 = new RoomDto("R2", "Test name2", "Test");
        RoomDto testRoomDto3 = new RoomDto("R3", "Test name3", "Test");

        List<RoomDto> expectedList = new ArrayList<>();
        expectedList.add(testRoomDto1);
        expectedList.add(testRoomDto2);
        expectedList.add(testRoomDto3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms")
        ).andExpect(status().isOk()).andReturn();

        List<RoomDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<RoomDto>>() {
        });

        Assertions.assertTrue(resultList.containsAll(expectedList));
//        assertThat(resultList).containsAll(expectedList);
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
    void addRoomThrowsExceptionWithNonUniqueNumberValue() throws Exception {
        RoomDto testRoomDto1 = new RoomDto("R1", "Test name1", "Test");
        assertEquals(400,performRoomPostBadRequest(testRoomDto1).getResponse().getStatus(),
                "Non unique Room number should return bad request status");
    }


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
    public void viewRoomByIdTest(){
        when(roomService.viewRoom(Id)).thenReturn(Optional.of(room));
        assertEquals(roomController.viewRoom(Id).getBody(), roomDto);
    }

    @Test
    public void getAllRoomsTest(){
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        when(roomService.getAllRooms()).thenReturn(rooms);
        assertEquals(roomController.getAllRooms().size(), rooms.size());
    }

}