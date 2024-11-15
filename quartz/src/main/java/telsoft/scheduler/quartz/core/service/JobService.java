package telsoft.scheduler.quartz.core.service;

import jakarta.xml.bind.ValidationException;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import telsoft.scheduler.quartz.core.entity.JobParam;
import telsoft.scheduler.quartz.core.enums.ParamType;
import telsoft.scheduler.quartz.core.repository.*;
import telsoft.scheduler.quartz.core.exception.NotFoundException;
import telsoft.scheduler.quartz.core.dto.CreateJobRequest;
import telsoft.scheduler.quartz.core.dto.ParamJobDTO;
import telsoft.scheduler.quartz.core.dto.StartupModeRequest;
import telsoft.scheduler.quartz.core.dto.TriggerDetail;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class JobService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    ProjectService projectService;

    @Autowired
    TriggerService triggerService;

    @Autowired
    private CronTriggerRepository cronTriggerRepository;

    @Autowired
    JobDetailRepository jobDetailRepository;

    @Autowired
    SimpleTriggerRepository simpleTriggerRepository;

    @Autowired
    TriggerRepository triggerRepository;

    @Autowired
    JobParamRepository jobParamRepository;


    public void createGenericJob(CreateJobRequest createJobRequest) throws SchedulerException, ClassNotFoundException, ValidationException, InterruptedException {
        // Validate request
        validateRequest(createJobRequest);

        String jobId = "job_" + System.currentTimeMillis();

        // Create JobDetail with params
        org.quartz.JobDetail jobDetail = buildJobDetail(jobId, createJobRequest);

        // Create triggers with params
        List<Trigger> triggers = createTriggers(createJobRequest);

        // Schedule jobDetail with triggers
        scheduler.scheduleJob(jobDetail, new HashSet<>(triggers), false);

        // Pause job
        scheduler.pauseJob(new JobKey(jobId, createJobRequest.getGroup()));

        // Add params after create job
        addJobParam(createJobRequest, jobId);
    }

    private JobDetail buildJobDetail(String jobId, CreateJobRequest createJobRequest) throws ClassNotFoundException {
        // Load jobClass by classpath
        Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(createJobRequest.getClasspath());

        // Add default jobData
        Map<String, String> jobData = new HashMap<>();
        jobData.put("jobAlias", createJobRequest.getJobAlias());
        jobData.put("debug", String.valueOf(createJobRequest.isDebug()));

        // Build Job
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobId, createJobRequest.getGroup())
                .withDescription(createJobRequest.getDescription())
                .usingJobData(new JobDataMap(jobData))
                .storeDurably(true)  // Đảm bảo job được lưu trong scheduler
                .requestRecovery(true)  // Bật chế độ tự phục hồi job nếu có lỗi
                .build();
    }

    private List<Trigger> createTriggers(CreateJobRequest createJobRequest) throws InterruptedException {
        List<Trigger> triggers = new ArrayList<>();
        for (TriggerDetail triggerDetailInfo : createJobRequest.getTriggerDetailList()) {
            Trigger trigger;

            switch (triggerDetailInfo.getTriggerType()) {
                case SIMPLE -> {
                    trigger = TriggerBuilder.newTrigger()
                            .withIdentity("trigger_" + System.currentTimeMillis(), createJobRequest.getGroup())
                            .withDescription(triggerDetailInfo.getDescription())
                            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(triggerDetailInfo.getIntervalSeconds())
                                    .repeatForever())
                            .build();
                }
                case CRON -> {
                    if (triggerDetailInfo.getCronExpression() == null)
                        throw new NotFoundException("Not found cron expression for CRON trigger");

                    trigger = TriggerBuilder.newTrigger()
                            .withIdentity("trigger_" + System.currentTimeMillis(), createJobRequest.getGroup())
                            .withDescription(triggerDetailInfo.getDescription())
                            .withSchedule(CronScheduleBuilder.cronSchedule(triggerDetailInfo.getCronExpression()))
                            .build();

                }
                default ->
                        throw new NotFoundException("Not support trigger type: " + triggerDetailInfo.getTriggerType());
            }

            triggers.add(trigger);

            // Sleep 10 millisecond for trigger identity
            Thread.sleep(10);
        }
        return triggers;
    }

    private void addJobParam(CreateJobRequest createJobRequest, String jobId) {
        telsoft.scheduler.quartz.core.entity.JobDetail jobSaved = jobDetailRepository.findById(jobId).get();
        // Add information for job
        jobSaved.setProjectName(createJobRequest.getProjectName());
        jobSaved.setJobAlias(createJobRequest.getJobAlias());
        jobSaved.setIsDebug(createJobRequest.isDebug());

        List<JobParam> paramList = new ArrayList<>();
        for (Map.Entry<String, ParamJobDTO> entry : createJobRequest.getParamsJob().entrySet()) {
            paramList.add(buildJobParam(jobSaved, entry.getKey(), entry.getValue().getParamType(), entry.getValue().getParamValue()));
        }

        jobParamRepository.saveAllAndFlush(paramList);
        jobDetailRepository.saveAndFlush(jobSaved);
    }

    private JobParam buildJobParam(telsoft.scheduler.quartz.core.entity.JobDetail jobDetail, String paramName, ParamType paramType, Object defaultValue) {
        return JobParam.builder()
                .jobName(jobDetail.getJobName())
                .paramName(paramName)
                .paramType(paramType)
                .paramValue(defaultValue.toString())
                .build();
    }

    public telsoft.scheduler.quartz.core.entity.JobDetail getJobDetail(String jobId) {
        Optional<telsoft.scheduler.quartz.core.entity.JobDetail> jobDetailDatabase = jobDetailRepository.findById(jobId);

        if (jobDetailDatabase.isEmpty())
            throw new NotFoundException("Not found job detail with jobId: " + jobId);

        return getJobDetails(jobDetailDatabase.get());
    }

    private telsoft.scheduler.quartz.core.entity.JobDetail getJobDetails(telsoft.scheduler.quartz.core.entity.JobDetail jobDetailDatabase) {
        // Set params for job
        List<JobParam> jobParams = jobParamRepository.findAll();
        jobDetailDatabase.setJobParams(jobParams);

        // Set triggers for job
        Set<telsoft.scheduler.quartz.core.entity.Trigger> triggers = getTriggersDetail(jobDetailDatabase.getJobName());
        jobDetailDatabase.setTriggers(triggers);
        return jobDetailDatabase;
    }

    private Set<telsoft.scheduler.quartz.core.entity.Trigger> getTriggersDetail(String jobId) {
        Set<telsoft.scheduler.quartz.core.entity.Trigger> triggers = triggerService.getTriggersListByJobName(jobId);
        for (telsoft.scheduler.quartz.core.entity.Trigger trigger : triggers) {
            trigger.setSimpleTriggers(simpleTriggerRepository.findByTriggerName(trigger.getTriggerName()));
            trigger.setCronTriggers(cronTriggerRepository.findAllByTriggerName(trigger.getTriggerName()));
        }
        return triggers;
    }

    private void validateRequest(CreateJobRequest request) throws ValidationException {
        if (projectService.getProjectByName(request.getProjectName()).isEmpty())
            throw new ValidationException("Not found project name: " + request.getProjectName());
    }

    public void deleteJobById(List<String> jobIds) {
        for (String jobId : jobIds) {
            telsoft.scheduler.quartz.core.entity.JobDetail jobDetail = getJobDetail(jobId);

            for (telsoft.scheduler.quartz.core.entity.Trigger trigger : jobDetail.getTriggers()) {

                switch (trigger.getTriggerType()) {
                    case "SIMPLE":
                        simpleTriggerRepository.deleteById(trigger.getTriggerName());
                    case "CRON":
                        cronTriggerRepository.deleteById(trigger.getTriggerName());
                }
                triggerRepository.deleteById(trigger.getTriggerName());
            }

            jobDetailRepository.deleteById(jobId);
            jobParamRepository.deleteJobParamsByJobName(jobId);

        }
    }

    public void pauseJob(String jobId) throws SchedulerException {
        Optional<telsoft.scheduler.quartz.core.entity.JobDetail> jobDatabase = jobDetailRepository.findById(jobId);
        if (jobDatabase.isEmpty())
            throw new NotFoundException("Not found job with jobId: " + jobId);

        JobKey jobKey = new JobKey(jobId, jobDatabase.get().getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    public void startJob(String jobId) throws SchedulerException {
        Optional<telsoft.scheduler.quartz.core.entity.JobDetail> jobDatabase = jobDetailRepository.findById(jobId);
        if (jobDatabase.isEmpty())
            throw new NotFoundException("Not found job with jobId: " + jobId);

        JobKey jobKey = new JobKey(jobId, jobDatabase.get().getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    public ResponseEntity<?> updateStartupMode(StartupModeRequest startupModeRequest) throws SchedulerException {
        scheduler.pauseJob(new JobKey(startupModeRequest.getJobName()));
        return ResponseEntity.ok("Startup mode updated successfully.");
    }

    public List<telsoft.scheduler.quartz.core.entity.JobDetail> getJobs(String projectName, String jobName, String jobGroup) throws NotFoundException, SchedulerException {
        telsoft.scheduler.quartz.core.entity.JobDetail exampleJobDetail = new telsoft.scheduler.quartz.core.entity.JobDetail();

        // Create example for search
        exampleJobDetail.setSchedName(scheduler.getSchedulerName());
        if (projectName != null && !projectName.isEmpty()) {
            exampleJobDetail.setProjectName(projectName);
        }
        if (jobName != null && !jobName.isEmpty()) {
            exampleJobDetail.setJobName(jobName);
        }
        if (jobGroup != null && !jobGroup.isEmpty()) {
            exampleJobDetail.setJobGroup(jobGroup);
        }

        // Search by example
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreNullValues();
        Example<telsoft.scheduler.quartz.core.entity.JobDetail> example = Example.of(exampleJobDetail, matcher);

        return jobDetailRepository.findAll(example);
    }

    public List<JobParam> findParamsByJobId(String jobId) {
        return jobParamRepository.findAllByJobName(jobId);
    }
}
