package telsoft.scheduler.quartz.core.log.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import telsoft.scheduler.quartz.core.entity.CronTrigger;
import telsoft.scheduler.quartz.core.entity.JobDetail;
import telsoft.scheduler.quartz.core.entity.JobHistory;
import telsoft.scheduler.quartz.core.entity.Trigger;
import telsoft.scheduler.quartz.core.service.JobService;
import telsoft.scheduler.quartz.core.service.TriggerService;

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
import java.util.Map;

import static telsoft.scheduler.quartz.core.log.quartz.JobContains.*;

@Component
@Description("Listener for logging")
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

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        String jobId = context.getJobDetail().getKey().getName();
        String message = "------------Started JobId: " + jobId;

        // Init params for JobHistory
        initJobContext(context, jobId);

        // Log to file
        logToFile(context, message);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        String message = "Job " + context.get(JOB_ID) + " was vetoed.";
        context.setResult("FAILED");

        afterJobExecuted(context, message);
    }


    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        String JobId = context.get(JOB_ID).toString();
        context.setResult("SUCCESS");

        String message = "JobId completed: " + JobId;
        if (jobException != null) {
            context.put(ERROR, jobException.getMessage());
            context.setResult("ERROR");
            message += ". Job encountered an exception: " + jobException.getMessage();
        }


        // Add params for JobHistory
        afterJobExecuted(context, message);
    }

    public void logToFile(JobExecutionContext context, String message) {
        // Add LOG for job
        context.put(LOG, context.get(LOG).toString() + message + "\n");

        // Get path log file
        String jobId = context.get(JOB_ID).toString();
        String jobAlias = jobService.getJobDetail(jobId).getJobAlias().trim().replaceAll(" ", "-");

        String logFileName = "logs/" + jobAlias + "/" + jobId + "_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".log";
        Path logFilePath = Paths.get(logFileName);

        // Lấy timestamp hiện tại và tên class
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        String className = context.getJobDetail().getJobClass().getName();
        String threadName = context.get(THREAD_NAME).toString();

        // Chuẩn bị thông điệp log với các thông tin bổ sung
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

    private void initJobContext(JobExecutionContext context, String jobId) {
        JobDetail jobDetail = jobService.getJobDetail(jobId);
        StringBuilder messageTrigger = buildTriggerMessage(context);

        context.put(JOB_ID, jobId);
        context.put(LOG, "");
        context.put(JOB_DATA, jobService.getMapParamsByJobId(jobId));
        context.put(JOB_TRIGGER, messageTrigger.toString());
        context.put(THREAD_NAME, Thread.currentThread().getName());
        context.put(DEBUG, jobDetail.getIsDebug());
    }

    private StringBuilder buildTriggerMessage(JobExecutionContext context) {
        String triggerId = context.getTrigger().getKey().getName();
        Trigger jobTrigger = triggerService.getTriggerById(triggerId).get();
        String triggerType = jobTrigger.getTriggerType();

        StringBuilder messageTrigger = new StringBuilder("Trigger type: " + triggerType + ", data: ");

        if (triggerType.equals("SIMPLE")) {
            messageTrigger.append(jobTrigger.getSimpleTriggers().getRepeatInterval());
        } else if (triggerType.equals("CRON")) {
            for (CronTrigger cronTrigger : jobTrigger.getCronTriggers()) {
                messageTrigger.append(cronTrigger.toString());
            }
        }
        return messageTrigger;
    }

    private void afterJobExecuted(JobExecutionContext context, String message) {
        // Add params for JobHistory
        executedJobHistory(context);

        // Log to file
        logToFile(context, message);

        // Save JobHistory
        saveJobHistory(context);
    }

    private void executedJobHistory(JobExecutionContext context) {
        context.put(FINISHED_AT, new Date());
        Date startedAt = context.getFireTime();
        Date finishedAt = (Date) context.get(FINISHED_AT);
        long duration = finishedAt.getTime() - startedAt.getTime();

        context.put(DURATION, duration);
        context.put(JOB_STATUS, context.getResult());
    }

    private void saveJobHistory(JobExecutionContext context) {
        String error = context.get(ERROR) != null ? context.get(ERROR).toString() : "";

        try {
            // Build JobHistory
            JobHistory jobHistory = JobHistory
                    .builder()
                    .jobId(context.get(JOB_ID).toString())
                    .startedAt(context.getFireTime())
                    .finishedAt((Date) context.get(FINISHED_AT))
                    .duration((long) context.get(DURATION))
                    .jobStatus(context.get(JOB_STATUS).toString())
                    .jobData(context.get(JOB_DATA).toString())
                    .jobTrigger(context.get(JOB_TRIGGER).toString())
                    .log(context.get(LOG).toString())
                    .instanceName(context.getScheduler().getSchedulerInstanceId())
                    .threadName(context.get(THREAD_NAME).toString())
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
