package telsoft.demo.quartz.core.dto;

import lombok.Data;

@Data
public class PauseJobRequest {
    private String jobName;
    private String jobGroup;
}
