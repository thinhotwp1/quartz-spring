package telsoft.scheduler.quartz.core.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

@Data
public class CreateJobRequest {
    @NonNull
    private String classpath;
    @NonNull
    private String projectName;
    @NonNull
    private String jobAlias;
    @NonNull
    private String group;
    private String description;
    private List<TriggerDetail> triggerDetailList;
    private boolean autoStart;
    private boolean debug;
    private Map<String, ParamJobDTO> paramsJob;
}

