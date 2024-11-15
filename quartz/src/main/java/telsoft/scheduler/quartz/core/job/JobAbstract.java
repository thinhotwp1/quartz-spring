package telsoft.scheduler.quartz.core.job;

import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telsoft.scheduler.quartz.core.entity.JobDetail;
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
        JobDetail jobDetail = jobService.getJobDetail(context.getJobDetail().getKey().getName());
        boolean debug = jobDetail.getIsDebug();
        if (debug) {
            quartzJobLogListener.logToFile(context, message);
        }
    }

    protected Map<String, Object> getParamsJob(JobExecutionContext context) {
        return jobService.getMapParamsByJobId(context.getJobDetail().getKey().getName());
    }

}
