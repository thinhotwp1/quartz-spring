package telsoft.scheduler.quartz.core.dto;

import lombok.Data;

@Data
public class StartupModeRequest {
    private String jobName;
    private boolean auto;
}
