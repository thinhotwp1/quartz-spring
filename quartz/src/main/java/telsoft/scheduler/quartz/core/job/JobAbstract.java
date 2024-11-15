package telsoft.scheduler.quartz.core.job;

import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telsoft.scheduler.quartz.core.entity.JobParam;
import telsoft.scheduler.quartz.core.log.quartz.QuartzJobLogListener;
import telsoft.scheduler.quartz.core.service.JobService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public abstract class JobAbstract implements JobInterface {

    @Autowired
    protected Scheduler scheduler;

    @Autowired
    JobService jobService;

    @Autowired
    QuartzJobLogListener quartzJobLogListener;

    public void logJob(JobExecutionContext context, String message) {
        quartzJobLogListener.logToFile(context, message);
    }

    public void logDebug(JobExecutionContext context, String message) {
        boolean debug = context.getJobDetail().getJobDataMap().get("debug").toString().equalsIgnoreCase("true");
        if (debug) {
            quartzJobLogListener.logToFile(context, message);
        }
    }

    protected Map<String, Object> getParamsJob(JobExecutionContext context) {
        Map<String, Object> map = new HashMap<>();

        // Get params by jobId
        String jobId = context.getJobDetail().getKey().getName();
        List<JobParam> jobParams = jobService.findParamsByJobId(jobId);

        // Add params to map
        for (JobParam param : jobParams) {
            map.put(param.getParamName(), param.getParamValue());
        }

        return map;
    }

}
