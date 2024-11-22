package telsoft.scheduler.quartz.core.job;

import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import telsoft.scheduler.quartz.core.dto.ParamJobDTO;
import telsoft.scheduler.quartz.core.entity.JobDetail;
import telsoft.scheduler.quartz.core.enums.ParamType;
import telsoft.scheduler.quartz.core.log.quartz.JobContains;
import telsoft.scheduler.quartz.core.log.quartz.QuartzJobLogListener;
import telsoft.scheduler.quartz.core.service.JobService;

import java.util.List;
import java.util.Map;

@Component
public abstract class JobAbstract implements JobInterface {
    @Autowired
    JobService jobService;

    @Autowired
    QuartzJobLogListener quartzJobLogListener;

    public void logJob(JobExecutionContext context, String message) {
        quartzJobLogListener.logToFile(context, message);
    }

    public void logDebug(JobExecutionContext context, String message) {
        boolean debug = (boolean) context.get(JobContains.DEBUG);
        if (debug) {
            quartzJobLogListener.logToFile(context, message);
        }
    }

    protected Map<String, Object> getParamsJob(JobExecutionContext context) {
        return (Map<String, Object>) context.get(JobContains.JOB_DATA);
    }

    @Description("Update param job after executed, ex: last file executed, sequence,...")
    protected void updateParamJob(JobExecutionContext context, ParamType paramType, String key, Object value) {
        jobService.updateParamJob(context.get(JobContains.JOB_ID).toString(), paramType, key, value);
    }

    @Description("Update list params job after executed, ex: last file executed, sequence,...")
    protected void updateListParamJob(JobExecutionContext context, List<ParamJobDTO> paramJobList) {
        jobService.updateListParamJob(context.get(JobContains.JOB_ID).toString(), paramJobList);
    }

}
