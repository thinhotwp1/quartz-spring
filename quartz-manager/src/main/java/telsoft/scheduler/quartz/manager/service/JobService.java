package telsoft.scheduler.quartz.manager.service;

import jakarta.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import telsoft.scheduler.quartz.manager.entity.JobHistory;
import telsoft.scheduler.quartz.manager.entity.JobParam;
import telsoft.scheduler.quartz.manager.entity.SimpleTrigger;
import telsoft.scheduler.quartz.manager.entity.Trigger;
import telsoft.scheduler.quartz.manager.enums.JobState;
import telsoft.scheduler.quartz.manager.enums.ParamType;
import telsoft.scheduler.quartz.manager.enums.TriggerState;
import telsoft.scheduler.quartz.manager.enums.TriggerType;
import telsoft.scheduler.quartz.manager.exception.NotFoundException;
import telsoft.scheduler.quartz.manager.dto.CreateJobRequest;
import telsoft.scheduler.quartz.manager.dto.ParamJobDTO;
import telsoft.scheduler.quartz.manager.dto.StartupModeRequest;
import telsoft.scheduler.quartz.manager.dto.TriggerDetail;
import telsoft.scheduler.quartz.manager.exception.ValidateException;
import telsoft.scheduler.quartz.manager.repository.JobDetailRepository;
import telsoft.scheduler.quartz.manager.repository.JobHistoryRepository;
import telsoft.scheduler.quartz.manager.repository.JobParamRepository;

import java.util.*;

@Service
@Description("This class using for manage jobs")
public class JobService {
    
    @Value("${spring.quartz.scheduler-name}")
    private String schedulerName;

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

    @Autowired
    ClassPathService classPathService;


    public void createGenericJob(CreateJobRequest createJobRequest) throws ClassNotFoundException, ValidationException, InterruptedException {
        // Validate request
        validateRequest(createJobRequest);

        String jobId = "job_" + System.currentTimeMillis();

        // Save JobDetail
        saveJobDetail(jobId, createJobRequest);

        // Create triggers with params
        saveTriggers(jobId, createJobRequest);

        // Add params after create job
        addJobParam(createJobRequest, jobId);
    }

    private void saveJobDetail(String jobId, CreateJobRequest createJobRequest) throws ClassNotFoundException {
        // Build Job
        telsoft.scheduler.quartz.manager.entity.JobDetail jobDetail =
                telsoft.scheduler.quartz.manager.entity.JobDetail.builder()
                        .jobName(jobId)
                        .jobGroup(createJobRequest.getGroup())
                        .jobAlias(createJobRequest.getJobAlias())
                        .jobClassName(createJobRequest.getClasspath())
                        .jobData("#")
                        .isDisable(false)
                        .description(createJobRequest.getDescription())
                        .isDurable(true)
                        .isNonconcurrent(false)
                        .isUpdateData(false)
                        .requestsRecovery(false)
                        .projectName(createJobRequest.getProjectName())
                        .isDebug(createJobRequest.isDebug())
                        .schedName(scheduler.getSchedulerName())
                        .build();

        jobDetailRepository.saveAndFlush(jobDetail);
    }

    private void saveTriggers(String jobId, CreateJobRequest createJobRequest) throws InterruptedException, SchedulerException {
        for (TriggerDetail triggerDetailInfo : createJobRequest.getTriggerDetailList()) {

            switch (triggerDetailInfo.getTriggerType()) {
                case SIMPLE -> saveSimpleTrigger(jobId, createJobRequest, triggerDetailInfo);

                case CRON -> saveCronTrigger(jobId, createJobRequest, triggerDetailInfo);

                default ->
                        throw new NotFoundException("Not support trigger type: " + triggerDetailInfo.getTriggerType());
            }

            // Sleep 10 millisecond for trigger identity
            Thread.sleep(10);
        }

    }

    private void validateCronTrigger(TriggerDetail triggerDetailInfo) {
        if (triggerDetailInfo.getCronExpression() == null)
            throw new ValidateException("Not found cron expression for CRON trigger");
    }

    private void saveCronTrigger(String jobId, CreateJobRequest createJobRequest, TriggerDetail triggerDetailInfo) throws SchedulerException {
        // Validate Cron trigger
        validateCronTrigger(triggerDetailInfo);

        long startTime = new Date().getTime();
        String triggerName = "trigger_" + System.currentTimeMillis();

        triggerService.saveTrigger(
                telsoft.scheduler.quartz.manager.entity.Trigger.builder()
                        .triggerName(triggerName)
                        .triggerGroup(createJobRequest.getGroup())
                        .jobGroup(createJobRequest.getGroup())
                        .description(triggerDetailInfo.getDescription())
                        .nextFireTime(startTime)
                        .startTime(startTime)
                        .endTime(0L)
                        .misfireInstr(0)
                        .jobData("")
                        .priority(5)
                        .prevFireTime(-1L)
                        .triggerState(String.valueOf(TriggerState.PAUSE))
                        .triggerType(String.valueOf(TriggerType.CRON))
                        .jobName(jobId)
                        .jobGroup(createJobRequest.getGroup())
                        .schedName(scheduler.getSchedulerName())
                        .description(triggerDetailInfo.getDescription())
                        .build());


        triggerService.saveCronTrigger(telsoft.scheduler.quartz.manager.entity.CronTrigger.builder()
                .cronExpression(triggerDetailInfo.getCronExpression())
                .timeZoneId("Asia/Saigon")
                .schedName(scheduler.getSchedulerName())
                .triggerGroup(createJobRequest.getGroup())
                .triggerName(triggerName)
                .build());
    }

    private void saveSimpleTrigger(String jobId, CreateJobRequest createJobRequest, TriggerDetail triggerDetailInfo) throws SchedulerException {
        long startTime = new Date().getTime();
        String triggerName = "trigger_" + System.currentTimeMillis();

        triggerService.saveTrigger(
                telsoft.scheduler.quartz.manager.entity.Trigger.builder()
                        .triggerName(triggerName)
                        .triggerGroup(createJobRequest.getGroup())
                        .jobGroup(createJobRequest.getGroup())
                        .description(triggerDetailInfo.getDescription())
                        .nextFireTime(startTime)
                        .startTime(startTime)
                        .endTime(0L)
                        .misfireInstr(0)
                        .jobData("")
                        .priority(5)
                        .prevFireTime(-1L)
                        .triggerState(String.valueOf(TriggerState.PAUSE))
                        .triggerType(String.valueOf(TriggerType.SIMPLE))
                        .jobName(jobId)
                        .jobGroup(createJobRequest.getGroup())
                        .schedName(scheduler.getSchedulerName())
                        .description(triggerDetailInfo.getDescription())
                        .build());

        SimpleTrigger simpleTrigger = new SimpleTrigger().builder()
                .repeatInterval((long) triggerDetailInfo.getIntervalSeconds() * 1000)
                .timesTriggered(0L)
                .repeatCount(-1L)
                .triggerName(triggerName)
                .schedName(scheduler.getSchedulerName())
                .triggerGroup(createJobRequest.getGroup())
                .build();
        triggerService.saveSimpleTrigger(simpleTrigger);
    }

    private void addJobParam(CreateJobRequest createJobRequest, String jobId) {
        telsoft.scheduler.quartz.manager.entity.JobDetail jobSaved = jobDetailRepository.findById(jobId).get();

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

    public telsoft.scheduler.quartz.manager.entity.JobDetail getJobDetail(String jobId) {
        Optional<telsoft.scheduler.quartz.manager.entity.JobDetail> jobDetailDatabase = jobDetailRepository.findById(jobId);

        if (jobDetailDatabase.isEmpty())
            throw new NotFoundException("Not found job detail with jobId: " + jobId);

        return getJobDetails(jobDetailDatabase.get());
    }

    private telsoft.scheduler.quartz.manager.entity.JobDetail getJobDetails(telsoft.scheduler.quartz.manager.entity.JobDetail jobDetailDatabase) {
        // Set params for job
        List<JobParam> jobParams = jobParamRepository.findAll();
        jobDetailDatabase.setJobParams(jobParams);

        // Set triggers for job
        Set<telsoft.scheduler.quartz.manager.entity.Trigger> triggers = triggerService.getTriggersDetail(jobDetailDatabase.getJobName());
        jobDetailDatabase.setTriggers(triggers);

        return jobDetailDatabase;
    }


    private void validateRequest(CreateJobRequest request) throws ValidationException {
        if (projectService.getProjectByName(request.getProjectName()).isEmpty())
            throw new ValidationException("Not found project name: " + request.getProjectName());

        if (classPathService.getClasspathById(request.getClasspath()).isEmpty())
            throw new ValidationException("Not found classpath: " + request.getClasspath());

    }

    public void disableJob(List<String> jobIds) {
        pauseJob(jobIds);
        for (String jobId : jobIds) {
            telsoft.scheduler.quartz.manager.entity.JobDetail jobDetail = getJobDetail(jobId);
            jobDetail.setIsDisable(true);
            jobDetailRepository.saveAndFlush(jobDetail);
        }
    }

    public void enableJob(List<String> jobIds) {
        for (String jobId : jobIds) {
            telsoft.scheduler.quartz.manager.entity.JobDetail jobDetail = getJobDetail(jobId);
            jobDetail.setIsDisable(false);
            jobDetailRepository.saveAndFlush(jobDetail);
        }
    }

    public void deleteJobById(List<String> jobIds) {
        for (String jobId : jobIds) {
            telsoft.scheduler.quartz.manager.entity.JobDetail jobDetail = getJobDetail(jobId);

            for (telsoft.scheduler.quartz.manager.entity.Trigger trigger : jobDetail.getTriggers()) {

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
        List<telsoft.scheduler.quartz.manager.entity.JobDetail> jobDatabases = jobDetailRepository.findAllById(jobIds);

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
        for (String jobId : jobIds) {
            Set<Trigger> triggerSet = triggerService.getTriggersListByJobName(jobId);
            for (Trigger trigger : triggerSet) {
                if (trigger.getTriggerState().contains(JobState.PAUSE.toString())) {
                    trigger.setTriggerState(JobState.WAITING.toString());
                }
            }
            triggerService.saveAllTriggers(triggerSet);
        }
    }

    public ResponseEntity<?> updateStartupMode(StartupModeRequest startupModeRequest) throws SchedulerException {
        scheduler.pauseJob(new JobKey(startupModeRequest.getJobName()));
        return ResponseEntity.ok("Startup mode updated successfully.");
    }

    public List<telsoft.scheduler.quartz.manager.entity.JobDetail> getJobs(String projectName, String jobName, String jobGroup, boolean isDisable) throws NotFoundException, SchedulerException {
        telsoft.scheduler.quartz.manager.entity.JobDetail exampleJobDetail = new telsoft.scheduler.quartz.manager.entity.JobDetail();

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
        Example<telsoft.scheduler.quartz.manager.entity.JobDetail> example = Example.of(exampleJobDetail, matcher);

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
