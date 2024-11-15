package telsoft.scheduler.quartz.core.log.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@Description("Listener for logging")
public class QuartzJobLogListener implements JobListener {

    private static final Logger logger = LoggerFactory.getLogger(QuartzJobLogListener.class);

    @Override
    public String getName() {
        return "Quartz Job Execution Listener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        String JobId = context.getJobDetail().getKey().getName();

        String message = "------------Started JobId: " + JobId;

        logToFile(context, message);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        String message = "Job " + context.getJobDetail().getKey().getName() + " was vetoed.";
        logToFile(context, message);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        // Ghi log sau khi job thực thi
        String JobId = context.getJobDetail().getKey().getName();

        String message = "JobId completed: " + JobId;
        if (jobException != null) {
            message += ". Job encountered an exception: " + jobException.getMessage();
        }

        logToFile(context, message);
    }

    public void logToFile(JobExecutionContext context, String message) {
        Map<String, Object> param = context.getJobDetail().getJobDataMap();

        String jobAlias = param.get("jobAlias").toString().trim().replaceAll(" ", "-");
        String jobId = context.getJobDetail().getKey().getName();

        String logFileName = "logs/" + jobAlias + "/" + jobId + "_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".log";
        Path logFilePath = Paths.get(logFileName);

        // Lấy timestamp hiện tại và tên class
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        String className = context.getJobDetail().getJobClass().getName();
        String threadName = Thread.currentThread().getName();

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
            logger.info(message);
        } catch (IOException e) {
            logger.error("Error writing log to file", e);
        }
    }
}
