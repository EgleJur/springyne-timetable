package lt.techin.springyne.report;

import lt.techin.springyne.exception.ScheduleValidationException;
import lt.techin.springyne.lesson.Lesson;
import lt.techin.springyne.lesson.LessonRepository;
import lt.techin.springyne.schedule.Schedule;
import lt.techin.springyne.schedule.ScheduleRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    DateTimeFormatter weekNumberDateFormatter = DateTimeFormatter.ofPattern("MM.dd");

    public void exportScheduleToExcel(Long scheduleId, HttpServletResponse response) throws IOException {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleValidationException("Schedule does not exist",
                        "schedule id", "Schedule not found", scheduleId.toString()));

        List<Lesson> lessons = lessonRepository.findByScheduleIdOrderByLessonDateAscLessonTimeAsc(scheduleId);
        Map<LocalDate,List<Lesson>> lessonsGroupedByWeek = lessons.stream().collect(Collectors.groupingBy(lesson -> lesson.getLessonDate()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))));
        Map<LocalDate, List<Lesson>> lessonsByGroupSorted = new TreeMap<>(lessonsGroupedByWeek);

        int lessonsPerDay = schedule.getGroup().getShift().getEnds() - schedule.getGroup().getShift().getStarts() + 1;

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Schedule");

        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        Row nameHeader = sheet.createRow(1);
        Cell nameCell = nameHeader.createCell(0);
        nameCell.setCellValue(schedule.getName());
        nameCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,5));
        nameHeader = sheet.createRow(2);
        nameCell = nameHeader.createCell(0);
        nameCell.setCellValue(schedule.getStartDate() + " - " + schedule.getEndDate());
        nameCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(2,2,0,5));

        int weekdayHeaderRowNumber = 4;
        Row weekdayHeader = sheet.createRow(weekdayHeaderRowNumber);

        Cell headerCell = weekdayHeader.createCell(0);
        headerCell.setCellValue("Savaitė");
        headerCell.setCellStyle(headerStyle);

        headerCell = weekdayHeader.createCell(1);
        headerCell.setCellValue("Pirmadienis");
        headerCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(weekdayHeaderRowNumber,weekdayHeaderRowNumber,1,lessonsPerDay));

        headerCell = weekdayHeader.createCell(lessonsPerDay + 1);
        headerCell.setCellValue("Antradienis");
        headerCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(weekdayHeaderRowNumber,weekdayHeaderRowNumber,lessonsPerDay + 1,(2 * lessonsPerDay)));

        headerCell = weekdayHeader.createCell((2 * lessonsPerDay) + 1);
        headerCell.setCellValue("Trečiadienis");
        headerCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(weekdayHeaderRowNumber,weekdayHeaderRowNumber,(2 * lessonsPerDay) + 1,(3 * lessonsPerDay)));

        headerCell = weekdayHeader.createCell((3 * lessonsPerDay) + 1);
        headerCell.setCellValue("Ketvirtadienis");
        headerCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(weekdayHeaderRowNumber,weekdayHeaderRowNumber,(3 * lessonsPerDay) + 1,(4 * lessonsPerDay)));

        headerCell = weekdayHeader.createCell((4 * lessonsPerDay) + 1);
        headerCell.setCellValue("Penktadienis");
        headerCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(weekdayHeaderRowNumber,weekdayHeaderRowNumber,(4 * lessonsPerDay) + 1,(5 * lessonsPerDay)));

        Row lessonNumberHeader = sheet.createRow(weekdayHeaderRowNumber + 1);
        Cell lessonNumberCell = lessonNumberHeader.createCell(0);
        lessonNumberCell.setCellValue("");
        lessonNumberCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(weekdayHeaderRowNumber,weekdayHeaderRowNumber + 1,0,0));

        int lessonCellNumber = 1;
        for (int i = 1; i<=5; i++) {
            for (int j = schedule.getGroup().getShift().getStarts(); j <= schedule.getGroup().getShift().getEnds(); j++) {
                lessonNumberCell = lessonNumberHeader.createCell(lessonCellNumber);
                lessonNumberCell.setCellValue(j);
                lessonNumberCell.setCellStyle(headerStyle);
                lessonCellNumber++;
            }
        }
        CellRangeAddress headerRegion = new CellRangeAddress(weekdayHeaderRowNumber, weekdayHeaderRowNumber + 1, 0, lessonCellNumber - 1);
        RegionUtil.setBorderTop(BorderStyle.THIN, headerRegion, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, headerRegion, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, headerRegion, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, headerRegion, sheet);


        short[] selectedColors = new short[]{IndexedColors.CORAL.getIndex(), IndexedColors.LIGHT_TURQUOISE.getIndex(),
        IndexedColors.ROSE.getIndex(), IndexedColors.TAN.getIndex(), IndexedColors.LIGHT_YELLOW.getIndex(), IndexedColors.LIGHT_GREEN.getIndex(),
        IndexedColors.LAVENDER.getIndex(), IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex(), IndexedColors.LIME.getIndex(),
        IndexedColors.AQUA.getIndex(), IndexedColors.BLUE_GREY.getIndex(), IndexedColors.BROWN.getIndex(), IndexedColors.DARK_RED.getIndex(),
        IndexedColors.DARK_YELLOW.getIndex(), IndexedColors.GOLD.getIndex(), IndexedColors.GREY_25_PERCENT.getIndex(),
        IndexedColors.GREY_40_PERCENT.getIndex(), IndexedColors.LEMON_CHIFFON.getIndex(), IndexedColors.LIGHT_BLUE.getIndex(),
        IndexedColors.LIGHT_ORANGE.getIndex(), IndexedColors.MAROON.getIndex(), IndexedColors.PALE_BLUE.getIndex(),
        IndexedColors.ROYAL_BLUE.getIndex(), IndexedColors.SEA_GREEN.getIndex(), IndexedColors.SKY_BLUE.getIndex(), IndexedColors.TEAL.getIndex()};

        CellStyle weekNumberColumnStyle = workbook.createCellStyle();
        weekNumberColumnStyle.setAlignment(HorizontalAlignment.CENTER);

        int dataRowCount = weekdayHeaderRowNumber + 2;
        for (Map.Entry<LocalDate, List<Lesson>> entry : lessonsByGroupSorted.entrySet()) {

            Row dataRow = sheet.createRow(dataRowCount);
            Cell weekNumber = dataRow.createCell(0);
            weekNumber.setCellValue(entry.getKey().format(weekNumberDateFormatter) + " - " + entry.getKey().plusDays(4L).format(weekNumberDateFormatter));
            weekNumber.setCellStyle(weekNumberColumnStyle);

            for (Lesson lesson : entry.getValue()) {
                int dataCellNumber = ((lesson.getLessonDate().getDayOfWeek().getValue() - 1) * lessonsPerDay) + 1 + (lesson.getLessonTime()
                        - schedule.getGroup().getShift().getStarts());
                Cell dataCell = dataRow.createCell(dataCellNumber);
                dataCell.setCellValue(lesson.getSubject().getId());
                CellStyle dataCellStyle = workbook.createCellStyle();
                short colorIndex = (short) (lesson.getSubject().getId().shortValue() % selectedColors.length);
                dataCellStyle.setFillForegroundColor(selectedColors[colorIndex]);
                dataCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                dataCell.setCellStyle(dataCellStyle);
            }
            dataRowCount++;
        }
        sheet.autoSizeColumn(0);

        CellRangeAddress mondayRegion = new CellRangeAddress(weekdayHeaderRowNumber, dataRowCount-1, 1, lessonsPerDay);
        RegionUtil.setBorderTop(BorderStyle.THIN, mondayRegion, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, mondayRegion, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, mondayRegion, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, mondayRegion, sheet);

        CellRangeAddress tuesdayRegion = new CellRangeAddress(weekdayHeaderRowNumber, dataRowCount-1, lessonsPerDay + 1, 2 * lessonsPerDay);
        RegionUtil.setBorderTop(BorderStyle.THIN, tuesdayRegion, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, tuesdayRegion, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, tuesdayRegion, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, tuesdayRegion, sheet);

        CellRangeAddress wednesdayRegion = new CellRangeAddress(weekdayHeaderRowNumber, dataRowCount-1, 2 * lessonsPerDay + 1, 3 * lessonsPerDay);
        RegionUtil.setBorderTop(BorderStyle.THIN, wednesdayRegion, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, wednesdayRegion, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, wednesdayRegion, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, wednesdayRegion, sheet);

        CellRangeAddress thursdayRegion = new CellRangeAddress(weekdayHeaderRowNumber, dataRowCount-1, 3 * lessonsPerDay + 1, 4 * lessonsPerDay);
        RegionUtil.setBorderTop(BorderStyle.THIN, thursdayRegion, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, thursdayRegion, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, thursdayRegion, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, thursdayRegion, sheet);

        CellRangeAddress fridayRegion = new CellRangeAddress(weekdayHeaderRowNumber, dataRowCount-1, 4 * lessonsPerDay + 1, 5 * lessonsPerDay);
        RegionUtil.setBorderTop(BorderStyle.THIN, fridayRegion, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, fridayRegion, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, fridayRegion, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, fridayRegion, sheet);


        Map<Long,List<Lesson>> lessonsGroupedBySubject = lessons.stream().collect(Collectors.groupingBy(lesson -> lesson.getSubject().getId()));
        Map<Long, List<Lesson>> lessonsBySubjectSorted = new TreeMap<>(lessonsGroupedBySubject);
        int legendRowCount = dataRowCount +2;
        Row legendHeader = sheet.createRow(legendRowCount);
        sheet.addMergedRegion(new CellRangeAddress(legendRowCount,legendRowCount,0,3));
        Cell legendHeaderCell = legendHeader.createCell(0);
        legendHeaderCell.setCellValue("Dalykas");
        legendHeaderCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(legendRowCount,legendRowCount,4,6));
        legendHeaderCell = legendHeader.createCell(4);
        legendHeaderCell.setCellValue("Mokytojas");
        legendHeaderCell.setCellStyle(headerStyle);
        legendHeaderCell = legendHeader.createCell(7);
        legendHeaderCell.setCellValue("Kab.");
        legendHeaderCell.setCellStyle(headerStyle);
        legendRowCount++;


        for(Map.Entry<Long,List<Lesson>> entry : lessonsBySubjectSorted.entrySet()) {
            int legendCellNumber = 0;
            Row legendRow = sheet.createRow(legendRowCount);
            Cell legendCell = legendRow.createCell(legendCellNumber);
            legendCell.setCellValue(entry.getKey());
            legendCellNumber++;
            legendCell = legendRow.createCell(legendCellNumber);
            legendCell.setCellValue(entry.getValue().get(0).getSubject().getName());
            CellStyle legendCellStyle = workbook.createCellStyle();
            legendCellStyle.setFillForegroundColor(selectedColors[(short) entry.getValue().get(0).getSubject().getId().shortValue() % selectedColors.length]);
            legendCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            legendCell.setCellStyle(legendCellStyle);
            sheet.addMergedRegion(new CellRangeAddress(legendRowCount,legendRowCount,legendCellNumber,legendCellNumber + 2));
            legendCellNumber+=3;
            legendCell = legendRow.createCell(legendCellNumber);
            legendCell.setCellValue(entry.getValue().get(0).getTeacher().getName());
            sheet.addMergedRegion(new CellRangeAddress(legendRowCount,legendRowCount,legendCellNumber,legendCellNumber + 2));
            legendCellNumber+=3;
            legendCell = legendRow.createCell(legendCellNumber);
            legendCell.setCellValue(entry.getValue().get(0).getRoom().getName());
            legendRowCount++;
        }

        CellRangeAddress legendRegion = new CellRangeAddress(dataRowCount + 2, legendRowCount - 1, 0, 7);
        RegionUtil.setBorderTop(BorderStyle.THIN, legendRegion, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, legendRegion, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, legendRegion, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, legendRegion, sheet);

        String[] lessonTimeValues = new String[] {"8.00-8.45", "8.55-9.40", "9.50-10.35", "10.45-11.30", "12.00-12.45", "12.55-13.40",
        "13.50-14.35", "14.45-15.30", "15.40-16.25", "16.35-17.20", "17.30-18.15", "18.25-19.10", "19.20-20.05", "20.15-21.00"};

        legendRowCount += 2;
        Row timeLegendHeader = sheet.createRow(legendRowCount);
        Cell timeLegendHeaderCell = timeLegendHeader.createCell(0);
        timeLegendHeaderCell.setCellValue("Pamokų laikas");
        sheet.addMergedRegion(new CellRangeAddress(legendRowCount,legendRowCount,0,3));
        timeLegendHeaderCell.setCellStyle(headerStyle);
        legendRowCount++;

        for(int i = 1; i <= 14; i++) {
            Row timeLegendRow = sheet.createRow(legendRowCount);
            Cell timeLegendCell = timeLegendRow.createCell(0);
            timeLegendCell.setCellValue(i + " pamoka");
            sheet.addMergedRegion(new CellRangeAddress(legendRowCount,legendRowCount,0,1));
            timeLegendCell = timeLegendRow.createCell(2);
            timeLegendCell.setCellValue(lessonTimeValues[i-1]);
            sheet.addMergedRegion(new CellRangeAddress(legendRowCount,legendRowCount,2,3));
            legendRowCount++;
        }

        CellRangeAddress timeLegendRegion = new CellRangeAddress(legendRowCount - 15, legendRowCount -1, 0, 3);
        RegionUtil.setBorderTop(BorderStyle.THIN, timeLegendRegion, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, timeLegendRegion, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, timeLegendRegion, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, timeLegendRegion, sheet);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
