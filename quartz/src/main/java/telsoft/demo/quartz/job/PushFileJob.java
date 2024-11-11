package telsoft.demo.quartz.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import telsoft.demo.quartz.core.job.JobAbstract;

import java.util.Map;

@Component
public class PushFileJob extends JobAbstract {

    private static final Logger logger = LoggerFactory.getLogger(GetFileJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Lấy các tham số được truyền vào
        Map<String, Object> param = context.getJobDetail().getJobDataMap();

        // Log từng tham số trong Map
        String log = "Executing job classpath " + context.getJobDetail().getJobClass() + " with the following parameters: ";
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            log = log + entry.getKey() + ": " + entry.getValue() + ", ";
        }
        logger.info(log);
    }

}
