package telsoft.demo.quartz.core.service;

import jakarta.xml.bind.ValidationException;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import telsoft.demo.quartz.core.dto.*;
import telsoft.demo.quartz.core.repository.CronTriggerRepository;
import telsoft.demo.quartz.core.entity.JobDetail;
import telsoft.demo.quartz.core.entity.Trigger;
import telsoft.demo.quartz.core.enums.TriggerType;
import telsoft.demo.quartz.core.exception.NotFoundException;
import telsoft.demo.quartz.core.repository.JobDetailRepository;
import telsoft.demo.quartz.core.repository.SimpleTriggerRepository;
import telsoft.demo.quartz.core.repository.TriggerRepository;

import java.util.*;

@Service
public class JobService {


    @Autowired
    private Scheduler scheduler;

    @Autowired
    JobDetailRepository jobDetailRepository;

    @Autowired
    SimpleTriggerRepository simpleTriggerRepository;

    @Autowired
    TriggerRepository triggerRepository;

    @Autowired
    TriggerService triggerService;

    @Autowired
    private CronTriggerRepository cronTriggerRepository;

    public List<JobDetail> getAllJobs() throws SchedulerException, NotFoundException {
        List<JobDetail> responseList = new ArrayList<>();
        List<JobDetail> jobDetailList = jobDetailRepository.findAll();

        if (jobDetailList.isEmpty())
            throw new NotFoundException("Not found any job");

        for (JobDetail jobDetail : jobDetailList) {
            responseList.add(getJobDetails(jobDetail));
        }

        return responseList;
    }

    public JobDetail getJobDetail(String jobId, String jobGroup) throws SchedulerException {
        Optional<JobDetail> jobDetailDatabase = jobDetailRepository.findByJobNameAndJobGroup(jobId, jobGroup);

        if (jobDetailDatabase.isEmpty())
            throw new NotFoundException("Not found job detail with jobId: " + jobId);

        return getJobDetails(jobDetailDatabase.get());
    }

    private JobDetail getJobDetails(JobDetail jobDetailDatabase) throws SchedulerException {
        // Convert JobDataMap to Map<String, Object>
        org.quartz.JobDetail jobDetailQuartz = scheduler.getJobDetail(JobKey.jobKey(jobDetailDatabase.getJobName(), jobDetailDatabase.getJobGroup()));
        JobDataMap jobDataMap = jobDetailQuartz.getJobDataMap();
        Map<String, Object> dataMap = new HashMap<>(jobDataMap);
        jobDetailDatabase.setJobDataMap(dataMap);

        // Set triggers for job
        jobDetailDatabase.setTriggers(triggerService.getTriggersListByJobName(jobDetailDatabase.getJobName()));
        return jobDetailDatabase;
    }

    public void createGenericJob(CreateJobRequest createJobRequest) throws SchedulerException, ClassNotFoundException, ValidationException {
        // Load jobClass by ClassPath
        Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(createJobRequest.getClasspath());

        // Validate request
        validateJobData(createJobRequest.getData());

        // Create JobDetail with params
        org.quartz.JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity("job_" + System.currentTimeMillis(), createJobRequest.getGroup())
                .withDescription(createJobRequest.getDescription())
                .usingJobData(new JobDataMap(createJobRequest.getData()))
                .storeDurably(true)  // Đảm bảo job được lưu trong scheduler
                .requestRecovery(true)  // Bật chế độ tự phục hồi job nếu có lỗi
                .build();

        // Create triggers with params
        List<org.quartz.Trigger> triggers = new ArrayList<>();
        for (TriggerDetail triggerDetailInfo : createJobRequest.getTriggerDetailList()) {
            org.quartz.Trigger trigger;
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

    private void validateJobData(Map<String, Object> data) throws ValidationException {
        if (data.get("debug") == null && data.get("jobName") == null)
            throw new ValidationException("Missing some default parameters: debug, jobName");
    }

    public void deleteJobById(String jobId, String jobGroup) throws SchedulerException {
        JobDetail jobDetail = getJobDetail(jobId, jobGroup);

        for (Trigger trigger : jobDetail.getTriggers()) {

            switch (trigger.getTriggerType()) {
                case "SIMPLE":
                    simpleTriggerRepository.deleteById(trigger.getTriggerName());
                case "CRON":
                    cronTriggerRepository.deleteById(trigger.getTriggerName());
            }
            triggerRepository.deleteById(trigger.getTriggerName());
        }

        jobDetailRepository.deleteById(jobId);
    }

    public void pauseJob(String jobId, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobId, jobGroup);
        scheduler.pauseJob(jobKey);
    }

    public ResponseEntity<?> updateStartupMode(StartupModeRequest startupModeRequest) throws SchedulerException {
        scheduler.pauseJob(new JobKey(startupModeRequest.getJobName()));
        return ResponseEntity.ok("Startup mode updated successfully.");
    }

    public void startJob(String jobId, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobId, jobGroup);
        scheduler.resumeJob(jobKey);
    }
}
