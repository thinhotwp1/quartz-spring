package telsoft.scheduler.quartz.manager.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

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
    private boolean debug;
    private String description;
    private List<TriggerDetail> triggerDetailList;
    private boolean autoStart;
    private List<ParamJobDTO> paramsJob;
}

