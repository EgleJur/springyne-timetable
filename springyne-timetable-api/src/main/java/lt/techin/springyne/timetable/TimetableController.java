package lt.techin.springyne.timetable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/timetable")
public class TimetableController {

    @Value("${application.version}")
    private String version;

    @GetMapping("/info")
    public String getInfo() {
        return version;
    }
}
