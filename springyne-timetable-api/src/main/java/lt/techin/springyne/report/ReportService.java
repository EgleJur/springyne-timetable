package lt.techin.springyne.report;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.lesson.Lesson;
import lt.techin.springyne.lesson.LessonRepository;
import lt.techin.springyne.schedule.Schedule;
import lt.techin.springyne.schedule.ScheduleRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    LessonRepository lessonRepository;

    public void exportScheduleToExcel(Long scheduleId, HttpServletResponse response) throws IOException {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleValidationException("Schedule does not exist",
                        "schedule id", "Schedule not found", scheduleId.toString()));

        List<Lesson> lessons = lessonRepository.findByScheduleIdOrderByLessonDateAscLessonTimeAsc(scheduleId);
        Map<LocalDate,List<Lesson>> lessonsGroupedByWeek = lessons.stream().collect(Collectors.groupingBy(lesson -> lesson.getLessonDate()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))));
        Map<LocalDate, List<Lesson>> lessonsSorted = new TreeMap<>(lessonsGroupedByWeek);

        int lessonsPerDay = schedule.getGroup().getShift().getEnds() - schedule.getGroup().getShift().getStarts() + 1;

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Schedule");
        Row weekdayHeader = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = weekdayHeader.createCell(0);
        headerCell.setCellValue("Savaitė");
        headerCell.setCellStyle(headerStyle);

        headerCell = weekdayHeader.createCell(1);
        headerCell.setCellValue("Pirmadienis");
        headerCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,lessonsPerDay));

        headerCell = weekdayHeader.createCell(lessonsPerDay + 1);
        headerCell.setCellValue("Antradienis");
        headerCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0,0,lessonsPerDay + 1,(2 * lessonsPerDay)));

        headerCell = weekdayHeader.createCell((2 * lessonsPerDay) + 1);
        headerCell.setCellValue("Trečiadienis");
        headerCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0,0,(2 * lessonsPerDay) + 1,(3 * lessonsPerDay)));

        headerCell = weekdayHeader.createCell((3 * lessonsPerDay) + 1);
        headerCell.setCellValue("Ketvirtadienis");
        headerCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0,0,(3 * lessonsPerDay) + 1,(4 * lessonsPerDay)));

        headerCell = weekdayHeader.createCell((4 * lessonsPerDay) + 1);
        headerCell.setCellValue("Penktadienis");
        headerCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0,0,(4 * lessonsPerDay) + 1,(5 * lessonsPerDay)));

        Row lessonNumberHeader = sheet.createRow(1);
        Cell lessonNumberCell = lessonNumberHeader.createCell(0);
        lessonNumberCell.setCellValue("");
        lessonNumberCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));

        int cellNumber = 1;
        for (int i = 1; i<=5; i++) {
            for (int j = schedule.getGroup().getShift().getStarts(); j <= schedule.getGroup().getShift().getEnds(); j++) {
                lessonNumberCell = lessonNumberHeader.createCell(cellNumber);
                lessonNumberCell.setCellValue(j);
                lessonNumberCell.setCellStyle(headerStyle);
                cellNumber++;
            }
        }

        CreationHelper createHelper = workbook.getCreationHelper();
        short format = createHelper.createDataFormat().getFormat("mm.dd");
        CellStyle weekNumberColumnStyle = workbook.createCellStyle();
        weekNumberColumnStyle.setDataFormat(format);

        int dataRowCount = 2;
        for (Map.Entry<LocalDate, List<Lesson>> entry : lessonsSorted.entrySet()) {

            Row dataRow = sheet.createRow(dataRowCount);
            Cell weekNumber = dataRow.createCell(0);
            weekNumber.setCellValue(entry.getKey());
            weekNumber.setCellStyle(weekNumberColumnStyle);

            for (Lesson lesson : entry.getValue()) {
                int dataCellNumber = ((lesson.getLessonDate().getDayOfWeek().getValue() - 1) * lessonsPerDay) + 1 + (lesson.getLessonTime()
                        - schedule.getGroup().getShift().getStarts());
                Cell dataCell = dataRow.createCell(dataCellNumber);
                dataCell.setCellValue(lesson.getSubject().getId());
            }
            dataRowCount++;
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
