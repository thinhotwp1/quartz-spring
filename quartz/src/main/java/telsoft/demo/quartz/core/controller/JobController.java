package telsoft.demo.quartz.core.controller;

import jakarta.xml.bind.ValidationException;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import telsoft.demo.quartz.core.dto.*;
import telsoft.demo.quartz.core.exception.NotFoundException;
import telsoft.demo.quartz.core.service.JobService;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static telsoft.demo.quartz.core.enums.TriggerType.CRON;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("/api/job")
public class JobController {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private JobService jobService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllJobs() throws SchedulerException, NotFoundException {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/get-detail")
    public ResponseEntity<?> getJobDetail(@RequestParam String jobId, @RequestParam String jobGroup) throws SchedulerException {
        return ResponseEntity.ok(jobService.getJobDetail(jobId, jobGroup));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGenericJob(@RequestBody CreateJobRequest createJobRequest) throws SchedulerException, ClassNotFoundException, ValidationException {
        jobService.createGenericJob(createJobRequest);
        return ResponseEntity.ok("Job created successfully with classpath: " + createJobRequest.getClasspath());
    }

    @PostMapping("/pause")
    public ResponseEntity<String> pauseJob(@RequestParam String jobId, @RequestParam String jobGroup) throws SchedulerException {
        jobService.pauseJob(jobId, jobGroup);
        return ResponseEntity.ok("Job paused successfully.");
    }

    @PostMapping("/start")
    public ResponseEntity<String> startJob(@RequestParam String jobId, @RequestParam String jobGroup) throws SchedulerException {
        jobService.startJob(jobId, jobGroup);
        return ResponseEntity.ok("Job started successfully.");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteJob(@RequestParam String jobId, @RequestParam String jobGroup) throws SchedulerException {
        jobService.deleteJobById(jobId, jobGroup);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/change-startup-mode")
    public ResponseEntity<?> changeStartupMode(@RequestBody StartupModeRequest startupModeRequest) throws SchedulerException {
        return jobService.updateStartupMode(startupModeRequest);
    }

    @GetMapping("/get-log")
    public ResponseEntity<StreamingResponseBody> getJobLogs(@RequestParam String jobName,
                                                            @RequestParam String jobId,
                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        LocalDate logDate = (date != null) ? date : LocalDate.now();
        String logFileName = "log/" + jobName.trim().replaceAll(" ", "-") + "/" + jobId + "_" + logDate.format(DateTimeFormatter.ISO_DATE) + ".log";

        StreamingResponseBody responseBody = outputStream -> {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(logFileName))) {
                String line;
                while (true) { // Keep streaming
                    line = reader.readLine();
                    if (line != null) {
                        outputStream.write((line + "\n\n").getBytes(StandardCharsets.UTF_8)); // SSE format
                        outputStream.flush();
                    } else {
                        Thread.sleep(1000); // Wait before checking for new lines
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM) // Set to SSE
                .body(responseBody);
    }

}

