package telsoft.demo.quartz.core.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class JobLogController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping("/logs/job")
    public List<String> getJobLogs(@RequestParam String jobName,
                                   @RequestParam String jobGroup,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws IOException {
        // Use the specified date, or default to today's date if none provided
        LocalDate logDate = (date != null) ? date : LocalDate.now();
        String logFileName = "logs/job-logs-" + logDate.format(DATE_FORMAT) + ".log";

        return Files.lines(Paths.get(logFileName))
                .filter(line -> line.contains(jobName) && line.contains(jobGroup))
                .collect(Collectors.toList());
    }
}

