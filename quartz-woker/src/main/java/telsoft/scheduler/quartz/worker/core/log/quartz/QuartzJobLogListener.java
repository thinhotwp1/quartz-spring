package telsoft.scheduler.quartz.worker.core.log.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import telsoft.scheduler.quartz.worker.core.entity.CronTrigger;
import telsoft.scheduler.quartz.worker.core.entity.JobDetail;
import telsoft.scheduler.quartz.worker.core.entity.Trigger;
import telsoft.scheduler.quartz.worker.core.enums.JobStatus;
import telsoft.scheduler.quartz.worker.core.service.JobService;
import telsoft.scheduler.quartz.worker.core.service.TriggerService;
import telsoft.scheduler.quartz.worker.core.entity.JobHistory;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Component
@Description("AOP: Listener for Job, do something before and after job executed, ex: Logging, store Job history,...")
public class QuartzJobLogListener implements JobListener {

    private static final Logger logger = LoggerFactory.getLogger(QuartzJobLogListener.class);

    @Autowired
    JobService jobService;

    @Autowired
    TriggerService triggerService;

    @Override
    public String getName() {
        return "Quartz Job Execution Listener";
    }

    @Description("AOP: Log & Init Job history before job executed")
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        String jobId = context.getJobDetail().getKey().getName();
        String message = "------------Started JobId: " + jobId;

        // Init params for JobHistory
        initJobContext(context, jobId);

        // Log to file
        logToFile(context, message);
    }

    @Description("AOP: Log & Save Job history after job error")
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        String message = "Job " + context.get(JobContains.JOB_ID) + " was vetoed.";
        context.setResult(JobStatus.ERROR.toString());

        afterJobExecuted(context, message);
    }

    @Description("AOP: Log & Save Job history after job executed")
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        String JobId = context.get(JobContains.JOB_ID).toString();
        context.setResult(JobStatus.SUCCESS.toString());

        String message = "JobId completed: " + JobId;
        if (jobException != null) {
            context.put(JobContains.ERROR, jobException.getMessage());
            context.setResult(JobStatus.ERROR.toString());
            message += ". Job encountered an exception: " + jobException.getMessage();
        }

        afterJobExecuted(context, message);
    }

    @Description("Write log to file")
    public void logToFile(JobExecutionContext context, String message) {
        // Add LOG for job
        context.put(JobContains.LOG, context.get(JobContains.LOG).toString() + message + "\n");

        // Get path log file
        String jobId = context.get(JobContains.JOB_ID).toString();
        String jobAlias = jobService.getJobDetail(jobId).getJobAlias().trim().replaceAll(" ", "-");

        String logFileName = "logs/" + jobAlias + "/" + jobId + "_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".log";
        Path logFilePath = Paths.get(logFileName);

        // Lấy timestamp hiện tại và tên class
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        String className = context.getJobDetail().getJobClass().getName();
        String threadName = context.get(JobContains.THREAD_NAME).toString();

        String logMessage = String.format("%s [%s] %s: %s", timestamp, threadName, className, message);

        // Đảm bảo thư mục tồn tại
        try {
            Files.createDirectories(logFilePath.getParent());
        } catch (IOException e) {
            logger.error("Error creating log directory", e);
        }

        // Ghi log vào file và log console
        try {
            Files.write(logFilePath, (logMessage + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
//            logger.info(message);
        } catch (IOException e) {
            logger.error("Error writing log to file", e);
        }
    }

    @Description("AOP: Init log for job")
    private void initJobContext(JobExecutionContext context, String jobId) {
        JobDetail jobDetail = jobService.getJobDetail(jobId);
        String messageTrigger = buildTriggerMessage(context);

        context.put(JobContains.JOB_ID, jobId);
        context.put(JobContains.LOG, "");
        context.put(JobContains.JOB_DATA, jobService.getMapParamsByJobId(jobId));
        context.put(JobContains.JOB_TRIGGER, messageTrigger);
        context.put(JobContains.THREAD_NAME, Thread.currentThread().getName());
        context.put(JobContains.DEBUG, jobDetail.getIsDebug());
    }

    private String buildTriggerMessage(JobExecutionContext context) {
        String triggerId = context.getTrigger().getKey().getName();
        Trigger jobTrigger = triggerService.getTriggerById(triggerId).get();
        String triggerType = jobTrigger.getTriggerType();

        StringBuilder messageTrigger = new StringBuilder("Trigger type: " + triggerType + ", data: ");

        if (triggerType.equals("SIMPLE")) {
            context.put(JobContains.TRIGGER_TYPE,"SIMPLE");
            messageTrigger.append(jobTrigger.getSimpleTriggers().getRepeatInterval()).append(" milliseconds");
        } else if (triggerType.equals("CRON")) {
            context.put(JobContains.TRIGGER_TYPE,"CRON");
            for (CronTrigger cronTrigger : jobTrigger.getCronTriggers()) {
                messageTrigger.append("cronExpression: ").append(cronTrigger.getCronExpression());
            }
        }
        return messageTrigger.toString();
    }

    private void afterJobExecuted(JobExecutionContext context, String message) {
        // Add data for JobHistory
        addDataJobHistory(context);

        // Log to file
        logToFile(context, message);

        // Save JobHistory
        saveJobHistory(context);
    }

    private void addDataJobHistory(JobExecutionContext context) {
        context.put(JobContains.FINISHED_AT, new Date());
        Date startedAt = context.getFireTime();
        Date finishedAt = (Date) context.get(JobContains.FINISHED_AT);
        long duration = finishedAt.getTime() - startedAt.getTime();

        context.put(JobContains.DURATION, duration);
        context.put(JobContains.JOB_STATUS, context.getResult());
    }

    @Description("Save Job history for trace")
    private void saveJobHistory(JobExecutionContext context) {
        String error = context.get(JobContains.ERROR) != null ? context.get(JobContains.ERROR).toString() : "";

        try {
            // Build JobHistory
            JobHistory jobHistory = JobHistory
                    .builder()
                    .jobId(context.get(JobContains.JOB_ID).toString())
                    .startedAt(context.getFireTime())
                    .finishedAt((Date) context.get(JobContains.FINISHED_AT))
                    .duration((long) context.get(JobContains.DURATION))
                    .jobStatus(context.get(JobContains.JOB_STATUS).toString())
                    .jobData(context.get(JobContains.JOB_DATA).toString())
                    .jobTrigger(context.get(JobContains.JOB_TRIGGER).toString())
                    .log(context.get(JobContains.LOG).toString())
                    .instanceName(context.getScheduler().getSchedulerInstanceId())
                    .threadName(context.get(JobContains.THREAD_NAME).toString())
                    .triggerType(context.get(JobContains.TRIGGER_TYPE).toString())
                    .jvmId(ManagementFactory.getRuntimeMXBean().getName().split("@")[0])
                    .error(error)
                    .build();

            jobService.saveJobHistory(jobHistory);
            logToFile(context, "Save job history success.");
        } catch (Exception e) {
            logToFile(context, "Save job history failed: " + e.getMessage());
        }
    }
}
