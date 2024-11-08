package telsoft.demo.quartz.core.dto;

import lombok.Data;
import telsoft.demo.quartz.core.enums.TriggerType;

@Data
public class TriggerDetail {
    private int intervalSeconds;
    private String cronExpression;  // Optional nếu muốn hỗ trợ cron trigger
    private TriggerType triggerType;
    private String description;
}
