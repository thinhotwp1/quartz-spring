package telsoft.scheduler.quartz.manager.dto;

import lombok.Data;
import telsoft.scheduler.quartz.manager.enums.ParamType;

@Data
public class ParamJobDTO {
    private ParamType paramType;
    private String paramName;
    private Object paramValue;
}
