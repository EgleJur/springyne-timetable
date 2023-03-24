package lt.techin.springyne.pdfExporter;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lt.techin.springyne.lesson.Lesson;
import lt.techin.springyne.room.Room;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class RoomLessonPdfExporter {
    private List<Lesson> listLessons;
    private Optional<Room> room;
    private String startDate;

    private String endDate;

    private BaseFont baseFont = BaseFont.createFont("fonts/LiberationSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);


    public RoomLessonPdfExporter(List<Lesson> listLessons, Optional<Room> room, String startDate, String endDate) throws IOException {
        this.listLessons = listLessons;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(6);

        Font font = new Font(baseFont, 12);

        cell.setPhrase(new Phrase("Data", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("Savaitės diena", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Pamoka", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Mokytojas", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Grupė", font));
        table.addCell(cell);
    }


    private void writeTableData(PdfPTable table) {
        Locale lithuanian = new Locale("lt", "LT");
        Font font = new Font(baseFont, 12);
        LocalDate sDate = LocalDate.parse(startDate);
        LocalDate eDate = LocalDate.parse(endDate);

        LocalDate previousLessonDate = null;
        LocalDate currentDate = sDate;

        while (!currentDate.isAfter(eDate)) {
            if (currentDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || currentDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                currentDate = currentDate.plusDays(1);
                continue;
            }

            boolean dateFound = false;
            for (Lesson lesson : listLessons) {
                LocalDate lessonDate = lesson.getLessonDate();

                if (currentDate.equals(lessonDate)) {
                    dateFound = true;
                    if (previousLessonDate == null || !previousLessonDate.equals(currentDate)) {
                        table.addCell(new PdfPCell(new Phrase(String.valueOf(lessonDate), font)));
                        table.addCell(new PdfPCell(new Phrase(lessonDate.getDayOfWeek().getDisplayName(TextStyle.FULL, lithuanian), font)));
                    } else {
                        table.addCell(new PdfPCell(new Phrase("", font)));
                        table.addCell(new PdfPCell(new Phrase("", font)));
                    }
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getLessonTime()), font)));
                    if (lesson.getTeacher() == null) {
                        table.addCell(new PdfPCell(new Phrase("Mokytojas nepriskirtas", font)));
                    } else {
                        table.addCell(new PdfPCell(new Phrase(lesson.getTeacher().getName(), font)));
                    }
                    table.addCell(new PdfPCell(new Phrase(lesson.getSchedule().getGroup().getName(), font)));
                    previousLessonDate = currentDate;
                }
            }

            if (!dateFound) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(currentDate), font)));
                table.addCell(new PdfPCell(new Phrase(currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, lithuanian), font)));
                table.addCell(new PdfPCell(new Phrase("", font)));
                table.addCell(new PdfPCell(new Phrase("", font)));
                table.addCell(new PdfPCell(new Phrase("", font)));
                previousLessonDate = null;
            }

            currentDate = currentDate.plusDays(1);
        }

    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = new Font(baseFont, 18);

        Paragraph p = new Paragraph(room.get().getName(), font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2.0f, 3.0f, 2.0f, 4.0f, 2.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}