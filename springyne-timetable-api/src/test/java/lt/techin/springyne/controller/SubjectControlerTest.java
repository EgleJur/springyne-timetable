//        package lt.techin.springyne.controller;
//
//        import com.fasterxml.jackson.core.type.TypeReference;
//        import com.fasterxml.jackson.databind.ObjectMapper;
//        import lt.techin.springyne.dto.SubjectDto;
//        import org.junit.jupiter.api.Assertions;
//        import org.junit.jupiter.api.Test;
//        import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//        import org.springframework.boot.test.context.SpringBootTest;
//        import org.springframework.http.MediaType;
//        import org.springframework.test.web.servlet.MockMvc;
//        import org.springframework.test.web.servlet.MvcResult;
//        import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//        import java.util.ArrayList;
//        import java.util.List;
//
//        import static org.junit.jupiter.api.Assertions.assertEquals;
//        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class SubjectControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Test
//    void getAllSubjectsContainsCorrectDtos() throws Exception {
//
//        SubjectDto testSubjectDto1 = new SubjectDto("T1", "Test name1", null, null);
//        SubjectDto testSubjectDto2 = new SubjectDto("T2", "Test name2", null, null);
//        SubjectDto testSubjectDto3 = new SubjectDto("T3", "Test name3", null, null);
//
//        List<SubjectDto> expectedList = new ArrayList<>();
//        expectedList.add(testSubjectDto1);
//        expectedList.add(testSubjectDto2);
//        expectedList.add(testSubjectDto3);
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/subject")
//        ).andExpect(status().isOk()).andReturn();
//
//        List<SubjectDto> resultList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<SubjectDto>>() {
//        });
//
//        Assertions.assertTrue(resultList.containsAll(expectedList));
//    }
//
//    @Test
//    void createSubjectThrowsExceptionWithNullOrEmptyValues() throws Exception {
//        SubjectDto testSubjectDto4 = new SubjectDto("", "Test name4", null, null);
//        SubjectDto testSubjectDto5 = new SubjectDto(null, "Test name5", null, null);
//        SubjectDto testSubjectDto6 = new SubjectDto("T6", "", null, null);
//        SubjectDto testSubjectDto7 = new SubjectDto("T7", null, null, null);
//
//        String message = "Null or empty values should return bad request status";
//
//        assertEquals(400,performSubjectPostBadRequest(testSubjectDto4).getResponse().getStatus(), message);
//        assertEquals(400,performSubjectPostBadRequest(testSubjectDto5).getResponse().getStatus(), message);
//        assertEquals(400,performSubjectPostBadRequest(testSubjectDto6).getResponse().getStatus(), message);
//        assertEquals(400,performSubjectPostBadRequest(testSubjectDto7).getResponse().getStatus(), message);
//    }
//
//    @Test
//    void addSubjectThrowsExceptionWithNonUniqueNumberValue() throws Exception {
//        SubjectDto testSubjectDto1 = new SubjectDto("T1", "Test", null, null);
//        assertEquals(400,performSubjectPostBadRequest(testSubjectDto1).getResponse().getStatus(),
//                "Non unique Module number should return bad request status");
//    }
//
//
//    public MvcResult performSubjectPostBadRequest(SubjectDto subjectDto) throws Exception {
//
//        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/subject").contentType(MediaType.APPLICATION_JSON).
//                        content(objectMapper.writeValueAsString(subjectDto)))
//                .andExpect(status().isBadRequest()).andReturn();
//    }
//
//}
