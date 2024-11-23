package telsoft.scheduler.quartz.manager.dto;

import lombok.Data;
import telsoft.scheduler.quartz.manager.enums.TriggerType;

@Data
public class TriggerDetail {
    private int intervalSeconds;
    private String cronExpression;  // Optional nếu muốn hỗ trợ cron trigger
    private TriggerType triggerType;
    private String description;
}
