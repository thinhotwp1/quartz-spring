package telsoft.demo.quartz.core.config;

import jakarta.annotation.PostConstruct;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.core.QuartzScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import telsoft.demo.quartz.core.log.QuartzJobLogListener;

@Configuration
@Description("Config for Quartz: init Scheduler, register listeners for logging,...")
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
