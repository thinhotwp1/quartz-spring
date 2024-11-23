package telsoft.scheduler.quartz.manager.dto;

import lombok.Data;

@Data
public class StartupModeRequest {
    private String jobName;
    private boolean auto;
}
