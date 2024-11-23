package telsoft.scheduler.quartz.worker.job.itlayer;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import telsoft.scheduler.quartz.worker.core.job.JobAbstract;

import java.util.Map;

@Component
public class PushFileJob extends JobAbstract {
    @Override
    public void execute(JobExecutionContext context) {
        // Lấy các tham số được truyền vào
        Map<String, Object> params = getParamsJob(context);

        // Job Business Execute
        jobBusinessExecute(context, params);
    }

    private void jobBusinessExecute(JobExecutionContext context, Map<String, Object> params) {
        String log = "Executing job classpath " + context.getJobDetail().getJobClass() + " with the following parameters: ";

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            log = log + entry.getKey() + ": " + entry.getValue() + ", ";
        }

        logDebug(context, log);
    }
}