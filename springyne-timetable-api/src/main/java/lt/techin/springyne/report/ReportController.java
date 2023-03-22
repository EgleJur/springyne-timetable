package lt.techin.springyne.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/reports")
public class ReportController {

    @Autowired
    ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/excel/schedule/{scheduleId}")
    public void exportScheduleToExcel(@PathVariable Long scheduleId, HttpServletResponse response) throws IOException {
//        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=schedule.xlsx");
        reportService.exportScheduleToExcel(scheduleId, response);
    }
}
