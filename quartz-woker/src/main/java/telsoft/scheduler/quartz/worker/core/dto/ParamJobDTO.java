package telsoft.scheduler.quartz.worker.core.dto;

import lombok.Data;
import telsoft.scheduler.quartz.worker.core.enums.ParamType;

@Data
public class ParamJobDTO {
    private ParamType paramType;
    private String paramName;
    private Object paramValue;
}
