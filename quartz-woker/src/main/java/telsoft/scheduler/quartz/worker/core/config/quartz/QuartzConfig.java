package telsoft.scheduler.quartz.worker.core.config.quartz;

import jakarta.annotation.PostConstruct;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import telsoft.scheduler.quartz.worker.core.log.quartz.QuartzJobLogListener;

@Configuration
@Description("Config AOP: init, logging,...")
public class QuartzConfig {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private QuartzJobLogListener jobLogListener;

    @PostConstruct
    public void registerListeners() throws SchedulerException {
        scheduler.getListenerManager().addJobListener(jobLogListener);
    }
}
