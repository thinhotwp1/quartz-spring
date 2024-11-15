package telsoft.scheduler.quartz.core.controller;

import jakarta.xml.bind.ValidationException;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import telsoft.scheduler.quartz.core.dto.*;
import telsoft.scheduler.quartz.core.dto.StartupModeRequest;
import telsoft.scheduler.quartz.core.service.JobService;
import telsoft.scheduler.quartz.core.dto.CreateJobRequest;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/create")
    public ResponseEntity<?> createGenericJob(@RequestBody CreateJobRequest createJobRequest) throws SchedulerException, ClassNotFoundException, ValidationException, InterruptedException {
        jobService.createGenericJob(createJobRequest);
        return ResponseEntity.ok("Job created successfully with classpath: " + createJobRequest.getClasspath());
    }

    @GetMapping("/get")
    public ResponseEntity<?> getJobs(@RequestParam(required = false) String projectName,
                                     @RequestParam(required = false) String jobName,
                                     @RequestParam(required = false) String jobGroup) throws SchedulerException {
        return ResponseEntity.ok(jobService.getJobs(projectName, jobName, jobGroup));
    }

    @GetMapping("/get-detail")
    public ResponseEntity<?> getJobDetail(@RequestParam String jobId) throws SchedulerException {
        return ResponseEntity.ok(jobService.getJobDetail(jobId));
    }

    @PostMapping("/pause")
    public ResponseEntity<String> pauseJob(@RequestBody List<String> jobIds) throws SchedulerException {
        jobService.pauseJob(jobIds);
        return ResponseEntity.ok("Job paused successfully.");
    }

    @PostMapping("/start")
    public ResponseEntity<String> startJob(@RequestBody List<String> jobIds) throws SchedulerException {
        jobService.startJob(jobIds);
        return ResponseEntity.ok("Job started successfully.");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteJob(@RequestBody List<String> jobIds) {
        jobService.deleteJobById(jobIds);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/change-startup-mode")
    public ResponseEntity<?> changeStartupMode(@RequestBody StartupModeRequest startupModeRequest) throws SchedulerException {
        return jobService.updateStartupMode(startupModeRequest);
    }

    @GetMapping("/get-log")
    public ResponseEntity<StreamingResponseBody> getJobLogs(@RequestParam String jobAlias,
                                                            @RequestParam String jobId,
                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        LocalDate logDate = (date != null) ? date : LocalDate.now();
        String logFileName = "logs/" + jobAlias.trim().replaceAll(" ", "-") + "/" + jobId + "_" + logDate.format(DateTimeFormatter.ISO_DATE) + ".log";

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

