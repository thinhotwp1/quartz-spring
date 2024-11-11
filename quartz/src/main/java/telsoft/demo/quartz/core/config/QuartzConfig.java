package telsoft.demo.quartz.core.config;

import jakarta.annotation.PostConstruct;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import telsoft.demo.quartz.core.log.QuartzJobLogListener;

@Configuration
public class QuartzConfig {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private QuartzJobLogListener jobExecutionListener;

    @PostConstruct
    public void registerListeners() throws SchedulerException {
        scheduler.getListenerManager().addJobListener(jobExecutionListener);
    }
}
