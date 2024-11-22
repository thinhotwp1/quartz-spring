package telsoft.scheduler.quartz.core.service;

import jakarta.xml.bind.ValidationException;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import telsoft.scheduler.quartz.core.entity.JobHistory;
import telsoft.scheduler.quartz.core.entity.JobParam;
import telsoft.scheduler.quartz.core.enums.ParamType;
import telsoft.scheduler.quartz.core.repository.*;
import telsoft.scheduler.quartz.core.exception.NotFoundException;
import telsoft.scheduler.quartz.core.dto.CreateJobRequest;
import telsoft.scheduler.quartz.core.dto.ParamJobDTO;
import telsoft.scheduler.quartz.core.dto.StartupModeRequest;
import telsoft.scheduler.quartz.core.dto.TriggerDetail;

import java.util.*;

@Service
public class JobService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    ProjectService projectService;

    @Autowired
    TriggerService triggerService;

    @Autowired
    JobDetailRepository jobDetailRepository;

    @Autowired
    JobParamRepository jobParamRepository;

    @Autowired
    JobHistoryRepository jobHistoryRepository;


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

        // Build Job
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobId, createJobRequest.getGroup())
                .withDescription(createJobRequest.getDescription())
                .usingJobData(new JobDataMap())
                .storeDurably(true)  // Save to database
                .requestRecovery(false)  // Recovery if error
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
        jobSaved.setIsDisable(false);

        List<JobParam> paramList = new ArrayList<>();
        for (ParamJobDTO jobParam : createJobRequest.getParamsJob()) {
            paramList.add(buildJobParam(jobSaved.getJobName(), jobParam.getParamType(), jobParam.getParamName(), jobParam.getParamValue().toString()));
        }

        jobParamRepository.saveAllAndFlush(paramList);
        jobDetailRepository.saveAndFlush(jobSaved);
    }

    private JobParam buildJobParam(String jobId, ParamType paramType, String paramName, String defaultValue) {
        return JobParam.builder()
                .jobName(jobId)
                .paramName(paramName)
                .paramType(paramType)
                .paramValue(defaultValue)
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
        Set<telsoft.scheduler.quartz.core.entity.Trigger> triggers = triggerService.getTriggersDetail(jobDetailDatabase.getJobName());
        jobDetailDatabase.setTriggers(triggers);

        return jobDetailDatabase;
    }


    private void validateRequest(CreateJobRequest request) throws ValidationException {
        if (projectService.getProjectByName(request.getProjectName()).isEmpty())
            throw new ValidationException("Not found project name: " + request.getProjectName());
    }

    public void disableJob(List<String> jobIds) {
        pauseJob(jobIds);
        for (String jobId : jobIds) {
            telsoft.scheduler.quartz.core.entity.JobDetail jobDetail = getJobDetail(jobId);
            jobDetail.setIsDisable(true);
            jobDetailRepository.saveAndFlush(jobDetail);
        }
    }

    public void enableJob(List<String> jobIds) {
        for (String jobId : jobIds) {
            telsoft.scheduler.quartz.core.entity.JobDetail jobDetail = getJobDetail(jobId);
            jobDetail.setIsDisable(false);
            jobDetailRepository.saveAndFlush(jobDetail);
        }
    }

    public void deleteJobById(List<String> jobIds) {
        for (String jobId : jobIds) {
            telsoft.scheduler.quartz.core.entity.JobDetail jobDetail = getJobDetail(jobId);

            for (telsoft.scheduler.quartz.core.entity.Trigger trigger : jobDetail.getTriggers()) {

                switch (trigger.getTriggerType()) {
                    case "SIMPLE":
                        triggerService.deleteSimpleTriggerById(trigger.getTriggerName());
                    case "CRON":
                        triggerService.deleteCronTriggerById(trigger.getTriggerName());
                }
                triggerService.deleteById(trigger.getTriggerName());
            }

            jobDetailRepository.deleteById(jobId);
            jobParamRepository.deleteJobParamsByJobName(jobId);

        }
    }

    public void pauseJob(List<String> jobIds) {
        List<telsoft.scheduler.quartz.core.entity.JobDetail> jobDatabases = jobDetailRepository.findAllById(jobIds);

        // Pause jobs
        jobDatabases.forEach(job -> {
            try {
                scheduler.pauseJob(new JobKey(job.getJobName(), job.getJobGroup()));
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void startJob(List<String> jobIds) {
        List<telsoft.scheduler.quartz.core.entity.JobDetail> jobDatabases = jobDetailRepository.findAllById(jobIds);

        // Start jobs
        jobDatabases.forEach(job -> {
            try {
                scheduler.resumeJob(new JobKey(job.getJobName(), job.getJobGroup()));
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public ResponseEntity<?> updateStartupMode(StartupModeRequest startupModeRequest) throws SchedulerException {
        scheduler.pauseJob(new JobKey(startupModeRequest.getJobName()));
        return ResponseEntity.ok("Startup mode updated successfully.");
    }

    public List<telsoft.scheduler.quartz.core.entity.JobDetail> getJobs(String projectName, String jobName, String jobGroup, boolean isDisable) throws NotFoundException, SchedulerException {
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
        exampleJobDetail.setIsDisable(isDisable);

        // Search by example
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreNullValues();
        Example<telsoft.scheduler.quartz.core.entity.JobDetail> example = Example.of(exampleJobDetail, matcher);

        return jobDetailRepository.findAll(example);
    }

    public Map<String, Object> getMapParamsByJobId(String jobId) {
        Map<String, Object> map = new HashMap<>();

        List<JobParam> jobParams = jobParamRepository.findAllByJobName(jobId);

        // Add params to map
        for (JobParam param : jobParams) {
            map.put(param.getParamName(), param.getParamValue());
        }
        return map;
    }

    public void saveJobHistory(JobHistory jobHistory) throws Exception {
        jobHistoryRepository.saveAndFlush(jobHistory);
    }

    public Object getJobHistory(String jobId, int limit) {
        List<JobHistory> jobHistories = jobHistoryRepository.findAllByJobIdOrderByStartedAtAsc(jobId);
        return jobHistories;
    }

    public void updateParamJob(String jobId, ParamType paramType, String key, Object value) {
        JobParam jobParam = JobParam
                .builder()
                .jobName(jobId)
                .paramType(paramType)
                .paramName(key)
                .paramValue(value.toString())
                .build();
        jobParamRepository.saveAndFlush(jobParam);
    }

    public void updateListParamJob(String jobId, List<ParamJobDTO> paramJobList) {
        List<JobParam> jobParams = new ArrayList<>();

        paramJobList.forEach(jobParam ->
                jobParams.add(buildJobParam(jobId, jobParam.getParamType(), jobParam.getParamName(), jobParam.getParamValue().toString())));

        jobParamRepository.saveAllAndFlush(jobParams);
    }
}
