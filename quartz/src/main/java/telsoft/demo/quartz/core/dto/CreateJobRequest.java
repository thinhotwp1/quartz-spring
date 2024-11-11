package telsoft.demo.quartz.core.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class CreateJobRequest {
    private String classpath;
    private String group;
    private String description;
    private Map<String, Object> data; // debug, jobName
    private List<TriggerDetail> triggerDetailList;
    private boolean autoStart;
}

