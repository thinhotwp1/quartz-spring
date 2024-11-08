package telsoft.demo.quartz.core.service;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import telsoft.demo.quartz.core.dto.*;
import telsoft.demo.quartz.core.entity.JobDetails;
import telsoft.demo.quartz.core.enums.TriggerType;
import telsoft.demo.quartz.core.exception.NotFoundException;
import telsoft.demo.quartz.core.repository.JobDetailsRepository;

import java.util.*;

@Service
public class JobService {


    @Autowired
    private Scheduler scheduler;

    @Autowired
    JobDetailsRepository jobDetailsRepository;

    @Autowired
    TriggerService triggerService;

    public List<JobDetails> getAllJobs() throws SchedulerException {
        List<JobDetails> responseList = new ArrayList<>();
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();

        if (jobDetailsList.isEmpty())
            throw new NotFoundException("Not found any job");

        for (JobDetails jobDetails : jobDetailsList) {
            responseList.add(getJobDetails(jobDetails));
        }

        return responseList;
    }

    public JobDetails getJobDetail(String jobName) throws SchedulerException {
        Optional<JobDetails> jobDetailDatabase = jobDetailsRepository.findById(jobName);

        if (jobDetailDatabase.isEmpty())
            throw new NotFoundException("Not found job detail with job name: " + jobName);

        return getJobDetails(jobDetailDatabase.get());
    }

    private JobDetails getJobDetails(JobDetails jobDetailDatabase) throws SchedulerException {
        // Convert JobDataMap to Map<String, Object>
        JobDetail jobDetailQuartz = scheduler.getJobDetail(JobKey.jobKey(jobDetailDatabase.getJobName(), jobDetailDatabase.getJobGroup()));
        JobDataMap jobDataMap = jobDetailQuartz.getJobDataMap();
        Map<String, Object> dataMap = new HashMap<>(jobDataMap);
        jobDetailDatabase.setJobDataMap(dataMap);

        // Set triggers for job
        jobDetailDatabase.setTriggers(triggerService.getTriggersListByJobName(jobDetailDatabase.getJobName()));
        return jobDetailDatabase;
    }

    public void createGenericJob(CreateJobRequest createJobRequest) throws SchedulerException, ClassNotFoundException {
        // Load jobClass by ClassPath
        Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(createJobRequest.getClasspath());

        // Create JobDetail with params
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity("job_" + System.currentTimeMillis(), createJobRequest.getGroup())
                .withDescription(createJobRequest.getDescription())
                .usingJobData(new JobDataMap(createJobRequest.getData()))
                .storeDurably(true)  // Đảm bảo job được lưu trong scheduler
                .requestRecovery(true)  // Bật chế độ tự phục hồi job nếu có lỗi
                .build();

        // Create triggers with params
        List<Trigger> triggers = new ArrayList<>();
        for (TriggerDetail triggerDetailInfo : createJobRequest.getTriggerDetailList()) {
            Trigger trigger;
            if (TriggerType.CRON.equals(triggerDetailInfo.getTriggerType()) && triggerDetailInfo.getCronExpression() != null) {
                // Create trigger CRON
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trigger_" + System.currentTimeMillis(), createJobRequest.getGroup())
                        .withDescription(triggerDetailInfo.getDescription())
                        .withSchedule(CronScheduleBuilder.cronSchedule(triggerDetailInfo.getCronExpression()))
                        .build();
            } else {
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trigger_" + System.currentTimeMillis(), createJobRequest.getGroup())
                        .withDescription(triggerDetailInfo.getDescription())
                        .startNow()
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(triggerDetailInfo.getIntervalSeconds())
                                .repeatForever())
                        .build();
            }
            triggers.add(trigger);
        }

        // Schedule jobDetail with triggers
        scheduler.scheduleJob(jobDetail, new HashSet<>(triggers), true);
    }

    public ResponseEntity<?> deleteJobById(String id) {
        try {
            jobDetailsRepository.deleteById(id);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public void pauseJob(PauseJobRequest pauseJobRequest) throws SchedulerException {
        JobKey jobKey = new JobKey(pauseJobRequest.getJobName(), pauseJobRequest.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    public ResponseEntity<?> updateStartupMode(StartupModeRequest startupModeRequest) throws SchedulerException {
        scheduler.pauseJob(new JobKey(startupModeRequest.getJobName()));
        return ResponseEntity.ok("Startup mode updated successfully.");
    }
}
