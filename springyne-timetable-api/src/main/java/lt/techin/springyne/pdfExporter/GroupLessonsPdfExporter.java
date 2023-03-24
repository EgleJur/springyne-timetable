package lt.techin.springyne.pdfExporter;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lt.techin.springyne.lesson.Lesson;
import lt.techin.springyne.schedule.Schedule;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class GroupLessonsPdfExporter {

    private final List<Lesson> lessonsBySchedule;

    private final Optional<Schedule> schedule;
//    private URL font_path = Thread.currentThread().getContextClassLoader().getResource("LiberationSans-Regular.ttf");
//    private URL font_path = Thread.currentThread().getContextClassLoader().getResource("LiberationSans-Regular.ttf");

//    private BaseFont baseFont = BaseFont.createFont("C:/Git/Springyne-timetable/springyne-timetable-api/fonts/LiberationSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//    private BaseFont baseFont = BaseFont.createFont(System.getProperty("user.dir") + "/src/main/resources/fonts/LiberationSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//    private BaseFont baseFont = BaseFont.createFont("src/main/resources/fonts/LiberationSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//Path path = Paths.get("/src/main/resources/fonts/LiberationSans-Regular.ttf");
//    Path absolutePath = path.toAbsolutePath().normalize();
private BaseFont baseFont = BaseFont.createFont("/fonts/LiberationSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//    private BaseFont baseFont = BaseFont.createFont(Matcher.quoteReplacement(System.getProperty("user.dir")) + "/src/main/resources/fonts/LiberationSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

//    Path path = Paths.get(Matcher.quoteReplacement("src/main/resources/fonts/LiberationSans-Regular.ttf"));
//    Path absolutePath = path.toAbsolutePath();
//    private BaseFont baseFont = BaseFont.createFont(absolutePath.toString(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

    public GroupLessonsPdfExporter(List<Lesson> lessonsBySchedule, Optional<Schedule> schedule) throws IOException {
        this.lessonsBySchedule = lessonsBySchedule;
        this.schedule = schedule;
//        FontFactory.register(System.getProperty("file.separator")+"resources"+System.getProperty("file.separator")+
//                "fonts"+System.getProperty("file.separator")+"LiberationSans-Regular.ttf", "springyne_font");
    }


    void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(5);

//        FontFactory.register("resources" + File.separator + "fonts" + File.separator + "LiberationSans-Regular.ttf", "springyne_font");
//        Font font = FontFactory.getFont("springyne_font", 12);
        Font font = new Font(baseFont, 12);

//        cell.setPhrase(new Phrase("Data"));
        cell.setPhrase(new Phrase("Data", font));
        table.addCell(cell);

//        cell.setPhrase(new Phrase("Laikas"));
        cell.setPhrase(new Phrase("Laikas", font));
        table.addCell(cell);

//        cell.setPhrase(new Phrase("Dalykas"));
        cell.setPhrase(new Phrase("Dalykas", font));
        table.addCell(cell);

//        cell.setPhrase(new Phrase("Mokytojas"));
        cell.setPhrase(new Phrase("Mokytojas", font));
        table.addCell(cell);

//        cell.setPhrase(new Phrase("Kabinetas"));
        cell.setPhrase(new Phrase("Kabinetas", font));
        table.addCell(cell);

    }

    void writeTableData(PdfPTable table) {
//        FontFactory.register("resources" + File.separator + "fonts" + File.separator + "LiberationSans-Regular.ttf", "springyne_font");
//        Font font = FontFactory.getFont("springyne_font", 12);
        Font font = new Font(baseFont, 12);

        for (Lesson lesson : lessonsBySchedule) {
//            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getLessonDate()))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getLessonDate()), font)));
//            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getLessonTime()))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getLessonTime()), font)));
//            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getSubject().getName()))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getSubject().getName()), font)));
//            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getTeacher().getName()))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getTeacher().getName()), font)));
//            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getRoom().getName()))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getRoom().getName()), font)));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

//        FontFactory.register("resources" + File.separator + "fonts" + File.separator + "LiberationSans-Regular.ttf", "springyne_font");
//        Font font = FontFactory.getFont("springyne_font", 18);
        Font font = new Font(baseFont, 18);

//        Paragraph paragraph = new Paragraph(schedule.get().getName());
        Paragraph paragraph = new Paragraph(schedule.get().getName(), font);
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