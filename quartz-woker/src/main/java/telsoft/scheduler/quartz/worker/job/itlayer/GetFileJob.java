package telsoft.scheduler.quartz.worker.job.itlayer;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import telsoft.scheduler.quartz.worker.core.job.JobAbstract;

import java.util.Map;

@Component
public class GetFileJob extends JobAbstract {
    @Override
    public void execute(JobExecutionContext context) {
        // Get params
        Map<String, Object> params = getParamsJob(context);

        // Job Business Execute
        jobBusinessExecute(context, params);
    }

    private void jobBusinessExecute(JobExecutionContext context, Map<String, Object> params) {
        String log = "Executing job with the following parameters: ";

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            log = log + entry.getKey() + ": " + entry.getValue() + ", ";
        }

        logDebug(context, log);
    }
}
