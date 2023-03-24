package lt.techin.springyne.pdfExporter;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lt.techin.springyne.lesson.Lesson;
import lt.techin.springyne.teacher.Teacher;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class TeacherLessonPdfExporter {
    private List<Lesson> listLessons;
    private Optional<Teacher> teacher;
//    Path path = Paths.get(Matcher.quoteReplacement("src/main/resources/fonts/LiberationSans-Regular.ttf"));
//    Path absolutePath = path.toAbsolutePath().normalize();

//    private URL font_path = Thread.currentThread().getContextClassLoader().getResource("LiberationSans-Regular.ttf");

    private BaseFont baseFont = BaseFont.createFont("/fonts/LiberationSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//    private BaseFont baseFont = BaseFont.createFont(System.getProperty("user.dir") + System.getProperty("file.separator") + "src" + System.getProperty("file.separator") + "main" +
//                System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "fonts" + System.getProperty("file.separator") + "LiberationSans-Regular.ttf",
//        BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//private BaseFont baseFont = BaseFont.createFont(absolutePath.toString(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//private BaseFont baseFont = BaseFont.createFont(Matcher.quoteReplacement(System.getProperty("user.dir")) + "/src/main/resources/fonts/LiberationSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

    public TeacherLessonPdfExporter(List<Lesson> listLessons, Optional<Teacher> teacher) throws IOException {
        this.listLessons = listLessons;
        this.teacher = teacher;
//        FontFactory.register(System.getProperty("file.separator")+"resources"+System.getProperty("file.separator")+
//                "fonts"+System.getProperty("file.separator")+"LiberationSans-Regular.ttf", "springyne_font");
    }

    public void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(6);

//        FontFactory.register("resources" + File.separator + "fonts" + File.separator + "LiberationSans-Regular.ttf", "springyne_font");
//        Font font = FontFactory.getFont("springyne_font",12);
       Font font = new Font(baseFont, 12);

        cell.setPhrase(new Phrase("Data", font));
//        cell.setPhrase(new Phrase("Data"));

        table.addCell(cell);

        cell.setPhrase(new Phrase("Savaitės diena", font));
//        cell.setPhrase(new Phrase("Savaitės diena"));
        table.addCell(cell);

//        cell.setPhrase(new Phrase("Laikas"));
        cell.setPhrase(new Phrase("Laikas", font));
        table.addCell(cell);

//        cell.setPhrase(new Phrase("Dalykas"));
        cell.setPhrase(new Phrase("Dalykas", font));
        table.addCell(cell);

//        cell.setPhrase(new Phrase("Grupė"));
        cell.setPhrase(new Phrase("Grupė", font));
        table.addCell(cell);

//        cell.setPhrase(new Phrase("Kabinetas"));
        cell.setPhrase(new Phrase("Kabinetas", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        Locale lithuanian = new Locale("lt", "LT");
//        FontFactory.register("resources" + File.separator + "fonts" + File.separator + "LiberationSans-Regular.ttf", "springyne_font");
//        Font font = FontFactory.getFont("springyne_font",12);
        Font font = new Font(baseFont, 12);

        for (Lesson lesson : listLessons) {

//            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getLessonDate()))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getLessonDate()), font)));

//            table.addCell(new PdfPCell(new Phrase(lesson.getLessonDate().getDayOfWeek().getDisplayName(TextStyle.FULL, lithuanian))));
            table.addCell(new PdfPCell(new Phrase(lesson.getLessonDate().getDayOfWeek().getDisplayName(TextStyle.FULL, lithuanian), font)));

//            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getLessonTime()))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getLessonTime()), font)));

//            table.addCell(new PdfPCell(new Phrase(lesson.getSubject().getName())));
            table.addCell(new PdfPCell(new Phrase(lesson.getSubject().getName(), font)));

//            table.addCell(new PdfPCell(new Phrase(lesson.getSchedule().getGroup().getName())));
            table.addCell(new PdfPCell(new Phrase(lesson.getSchedule().getGroup().getName(), font)));

//            table.addCell(new PdfPCell(new Phrase(lesson.getRoom().getName())));
            table.addCell(new PdfPCell(new Phrase(lesson.getRoom().getName(), font)));

        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
//        FontFactory.register("resources" + File.separator + "fonts" + File.separator + "LiberationSans-Regular.ttf", "springyne_font");
//        Font font = FontFactory.getFont("springyne_font",18);
       Font font = new Font(baseFont, 18);

//        Paragraph p = new Paragraph(teacher.get().getName());
        Paragraph p = new Paragraph(teacher.get().getName(), font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.0f, 3.0f, 2.0f, 4.0f, 2.0f, 2.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}
