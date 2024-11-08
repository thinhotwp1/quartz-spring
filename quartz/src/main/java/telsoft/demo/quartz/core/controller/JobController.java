package telsoft.demo.quartz.core.controller;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import telsoft.demo.quartz.core.dto.CreateJobRequest;
import telsoft.demo.quartz.core.dto.PauseJobRequest;
import telsoft.demo.quartz.core.dto.StartupModeRequest;
import telsoft.demo.quartz.core.dto.TriggerDetail;
import telsoft.demo.quartz.core.service.JobService;

import java.util.*;

import static telsoft.demo.quartz.core.enums.TriggerType.CRON;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllJobs() throws SchedulerException {
        return ResponseEntity.ok(jobService.getAllJobs());
    }
    @GetMapping("/get-job-detail")
    public ResponseEntity<?> getJobDetail(@RequestParam String jobName) throws SchedulerException {
        return ResponseEntity.ok(jobService.getJobDetail(jobName));
    }
    @PostMapping("/create")
    public ResponseEntity<?> createGenericJob(@RequestBody CreateJobRequest createJobRequest) throws SchedulerException, ClassNotFoundException {
        jobService.createGenericJob(createJobRequest);
        return ResponseEntity.ok("Job created successfully with classpath: " + createJobRequest.getClasspath());
    }
    @PostMapping("/pause-job")
    public ResponseEntity<String> pauseJob(@RequestBody PauseJobRequest pauseJobRequest) throws SchedulerException {
        jobService.pauseJob(pauseJobRequest);
        return ResponseEntity.ok("Job paused successfully.");
    }
    @PutMapping("/updateStartupMode")
    public ResponseEntity<?> updateStartupMode(@RequestBody StartupModeRequest startupModeRequest) throws SchedulerException {
        return jobService.updateStartupMode(startupModeRequest);
    }
    @PostMapping("/delete-job-by-id")
    public ResponseEntity<?> deleteJobById(@RequestParam String id) {
        return jobService.deleteJobById(id);
    }
}
