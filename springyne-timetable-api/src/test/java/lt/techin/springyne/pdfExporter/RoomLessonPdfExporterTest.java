package lt.techin.springyne.pdfExporter;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

import java.awt.*;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import lt.techin.springyne.group.Group;
import lt.techin.springyne.schedule.Schedule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;

import lt.techin.springyne.lesson.Lesson;
import lt.techin.springyne.room.Room;
import lt.techin.springyne.subject.Subject;
import lt.techin.springyne.teacher.Teacher;

public class RoomLessonPdfExporterTest {



    private Room room;




    @Test
    public void testExport() throws Exception {
        // Create a mock lesson
        Lesson lesson = mock(Lesson.class);
        when(lesson.getLessonDate()).thenReturn(LocalDate.now());
        when(lesson.getLessonTime()).thenReturn(1);
        Schedule schedule = mock(Schedule.class);
        when(lesson.getSchedule()).thenReturn(schedule);
        Group group = mock(Group.class);
        when(schedule.getGroup()).thenReturn(group);

        // Create a list of lessons
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);

        // Create mocks for subject, teacher and room
        Subject subject = mock(Subject.class);
        when(lesson.getSubject()).thenReturn(subject);
        when(subject.getName()).thenReturn("Tinklapiai");

        Teacher teacher = mock(Teacher.class);
        when(lesson.getTeacher()).thenReturn(teacher);
        when(teacher.getName()).thenReturn("Jonas Jonaitis");

        Room room = mock(Room.class);
        when(room.getName()).thenReturn("201");

        // Create a mock response
        HttpServletResponse response = new MockHttpServletResponse();

        // Create a new instance of the PDF exporter and call the export method
        RoomLessonPdfExporter pdfExporter = new RoomLessonPdfExporter(lessons, Optional.of(room), LocalDate.now().toString(), LocalDate.now().plusDays(7).toString());
        pdfExporter.export(response);

        // Assert that the response status code is 200
        assert(response.getStatus() == 200);
    }

    @Test
    public void testWriteTableHeader() throws Exception {
        // Given
        RoomLessonPdfExporter exporter = new RoomLessonPdfExporter(new ArrayList<>(), Optional.empty(), "2022-01-01", "2022-01-31");
        PdfPTable table = new PdfPTable(5);

        // When
        exporter.writeTableHeader(table);

        // Then
        List<PdfPCell> cells = List.of(table.getRow(0).getCells());
        Assertions.assertEquals("Data", cells.get(0).getPhrase().getContent());
        Assertions.assertEquals("Savaitės diena", cells.get(1).getPhrase().getContent());
        Assertions.assertEquals("Pamoka", cells.get(2).getPhrase().getContent());
        Assertions.assertEquals("Mokytojas", cells.get(3).getPhrase().getContent());
        Assertions.assertEquals("Grupė", cells.get(4).getPhrase().getContent());
    }
    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        room = mock(Room.class);
    }

    @Test
    public void testWriteTableHeader2() throws Exception {
        RoomLessonPdfExporter pdfExporter = new RoomLessonPdfExporter(new ArrayList<>(), Optional.of(room), "2022-01-01", "2022-01-31");

        PdfPTable table = new PdfPTable(5);
        pdfExporter.writeTableHeader(table);

        Assertions.assertEquals(5, table.getNumberOfColumns());

        PdfPCell cell = table.getRow(0).getCells()[0];
        Assertions.assertEquals(Color.LIGHT_GRAY, cell.getBackgroundColor());

        Font font = cell.getPhrase().getFont();
        Assertions.assertEquals(BaseFont.IDENTITY_H, font.getBaseFont().getEncoding());
        Assertions.assertEquals(12, font.getSize(), 0.0);
        assertFalse(font.isBold());
    }






}