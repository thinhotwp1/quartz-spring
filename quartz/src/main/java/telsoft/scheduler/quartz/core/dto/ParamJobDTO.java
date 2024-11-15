package telsoft.scheduler.quartz.core.dto;

import lombok.Data;
import telsoft.scheduler.quartz.core.enums.ParamType;

@Data
public class ParamJobDTO {
    private ParamType paramType;
    private String paramValue;
}
