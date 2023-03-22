package lt.techin.springyne.pdfExporter;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import lt.techin.springyne.group.Group;
import lt.techin.springyne.lesson.Lesson;
import lt.techin.springyne.room.Room;
import lt.techin.springyne.schedule.Schedule;
import lt.techin.springyne.subject.Subject;
import lt.techin.springyne.teacher.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TeacherLessonPdfExporterTest {
    private TeacherLessonPdfExporter teacherLessonPdfExporter;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Teacher teacher;

    @Mock
    private Lesson lesson;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExport() throws Exception {
        Lesson lesson = mock(Lesson.class);
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);
        when(lesson.getLessonDate()).thenReturn(LocalDate.of(2023,9,04));
        when(lesson.getLessonTime()).thenReturn(1);
        Subject subject = mock(Subject.class);
        when(lesson.getSubject()).thenReturn(subject);
        when(subject.getName()).thenReturn("Tinklapiai");

        Schedule schedule = mock(Schedule.class);
        Group group = mock(Group.class);
        when(lesson.getSchedule()).thenReturn(schedule);
        when(schedule.getGroup()).thenReturn(group);
        when(group.getName()).thenReturn("JP-22/1");

        Room room = mock(Room.class);
        when(lesson.getRoom()).thenReturn(room);
        when(room.getName()).thenReturn("201");

        when(teacher.getName()).thenReturn("Jonas Jonaitis");

        TeacherLessonPdfExporter pdfExporter = new TeacherLessonPdfExporter(lessons, Optional.of(teacher));

        MockHttpServletResponse response = new MockHttpServletResponse();

        pdfExporter.export(response);

}

    @Test
    void testWriteTableHeader() throws IOException {
        List<Lesson> lessons = new ArrayList<>();
        Optional<Teacher> teacher = Optional.of(new Teacher());

        teacherLessonPdfExporter = new TeacherLessonPdfExporter(lessons, teacher);

        PdfPTable table = new PdfPTable(6);

        teacherLessonPdfExporter.writeTableHeader(table);

        assertEquals(6, table.getNumberOfColumns());

        for (int i = 0; i < table.getNumberOfColumns(); i++) {
            PdfPCell cell = table.getRow(0).getCells()[i];
            assertNotNull(cell.getPhrase());
        }
    }

    @Test
    public void testWriteTableHeader2() throws Exception {
        TeacherLessonPdfExporter pdfExporter = new TeacherLessonPdfExporter(new ArrayList<>(), Optional.of(teacher));

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
