package telsoft.demo.quartz.core.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import telsoft.demo.quartz.core.log.QuartzJobLogListener;

public abstract class JobAbstract implements Job {

    public static void logJob(JobExecutionContext context, String message) {
        QuartzJobLogListener.logToFile(context, message);
    }

    public static void logDebug(JobExecutionContext context, String message) {
        boolean debug = context.getJobDetail().getJobDataMap().get("debug").toString().equalsIgnoreCase("Y");
        if (debug) {
            QuartzJobLogListener.logToFile(context, message);
        }
    }
}
