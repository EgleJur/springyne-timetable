package lt.techin.springyne.controller;

import lt.techin.springyne.lesson.LessonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LessonControllerMockDbTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private LessonService lessonService;

    @Test
    public void testDeleteSingleLessonSuccess() throws Exception {
        Long lessonId = 1L;
        when(lessonService.deleteLessonsByDateAndId(lessonId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/lessons/{lessonId}", lessonId))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteSingleLessonNotFound() throws Exception {
        Long lessonId = 1L;
        when(lessonService.deleteLessonsByDateAndId(lessonId)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/lessons/{lessonId}", lessonId))
                .andExpect(status().isNotFound());
    }

}
