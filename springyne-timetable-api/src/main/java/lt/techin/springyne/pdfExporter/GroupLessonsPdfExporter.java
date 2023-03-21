package lt.techin.springyne.pdfExporter;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.*;
import lt.techin.springyne.lesson.Lesson;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GroupLessonsPdfExporter {

    private List<Lesson> lessonsBySchedule;

    private BaseFont baseFont = BaseFont.createFont("fonts/LiberationSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

    public GroupLessonsPdfExporter(List<Lesson> lessonsBySchedule) throws IOException {
        this.lessonsBySchedule = lessonsBySchedule;
    }


    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.lightGray);
        cell.setPadding(5);

//        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        Font font = new Font(baseFont, 12);
//        Font font = FontFactory.getFont(FontFactory.HELVETICA);
//        font.setColor(Color.BLACK);

        cell.setPhrase(new Phrase("Data", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Laikas", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Dalykas", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Mokytojas", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Kabinetas", font));
        table.addCell(cell);

    }

    private void writeTableData(PdfPTable table) {
        Locale lithuanian = new Locale("lt", "LT");
//        PdfPCell cell = new PdfPCell();
        Font font = new Font(baseFont, 12);

        for (Lesson lesson : lessonsBySchedule) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getLessonDate()), font)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getLessonTime()), font)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getSubject().getName()), font)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getTeacher().getName()), font)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getRoom().getName()), font)));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = new Font(baseFont, 18);

        Paragraph paragraph = new Paragraph("Pamokų sąrašas", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2.5f, 1.5f, 4.0f, 3.0f, 2.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();


    }
}