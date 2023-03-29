package lt.techin.springyne.pdfExporter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import lt.techin.springyne.room.Room;
import lt.techin.springyne.subject.Subject;
import lt.techin.springyne.teacher.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lt.techin.springyne.lesson.Lesson;
import lt.techin.springyne.schedule.Schedule;
import org.springframework.mock.web.MockHttpServletResponse;

class GroupLessonsPdfExporterTest {
    private GroupLessonsPdfExporter groupLessonsPdfExporter;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Schedule schedule;

    @Mock
    private Lesson lesson;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExport() throws Exception {
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);

        Subject subject = mock(Subject.class);
        when(lesson.getSubject()).thenReturn(subject);
        when(subject.getName()).thenReturn("Tinklapiai");

        Teacher teacher = mock(Teacher.class);
        when(lesson.getTeacher()).thenReturn(teacher);
        when(teacher.getName()).thenReturn("Jonas Jonaitis");

        Room room = mock(Room.class);
        when(lesson.getRoom()).thenReturn(room);
        when(room.getName()).thenReturn("201");

        when(schedule.getName()).thenReturn("JP-22/1 Programinės įrangos testuotojas (-a) Rytinė");

        GroupLessonsPdfExporter pdfExporter = new GroupLessonsPdfExporter(lessons, Optional.of(schedule));

        MockHttpServletResponse response = new MockHttpServletResponse();

        pdfExporter.export(response);

    }

    @Test
    void testWriteTableHeader() throws IOException {
        List<Lesson> lessons = new ArrayList<>();
        Optional<Schedule> schedule = Optional.of(new Schedule());

        groupLessonsPdfExporter = new GroupLessonsPdfExporter(lessons, schedule);

        PdfPTable table = new PdfPTable(5);

        groupLessonsPdfExporter.writeTableHeader(table);

        assertEquals(5, table.getNumberOfColumns());

        for (int i = 0; i < table.getNumberOfColumns(); i++) {
            PdfPCell cell = table.getRow(0).getCells()[i];
            assertNotNull(cell.getPhrase());
        }
    }

    @Test
    public void testWriteTableHeader2() throws Exception {
        GroupLessonsPdfExporter pdfExporter = new GroupLessonsPdfExporter(new ArrayList<>(), Optional.of(schedule));

        PdfPTable table = new PdfPTable(5);
        pdfExporter.writeTableHeader(table);

        assertEquals(5, table.getNumberOfColumns());

        PdfPCell cell = table.getRow(0).getCells()[0];
        assertEquals(Color.LIGHT_GRAY, cell.getBackgroundColor());

        Font font = cell.getPhrase().getFont();
        assertEquals(BaseFont.IDENTITY_H, font.getBaseFont().getEncoding());
        assertEquals(12, font.getSize(), 0.0);
        assertFalse(font.isBold());

    }
}
