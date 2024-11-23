package telsoft.scheduler.quartz.manager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "QRTZ_CRON_TRIGGERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronTrigger extends BaseEntity {
    @Id
    @Column(name = "TRIGGER_NAME", nullable = false, length = 200)
    private String triggerName;

    @Column(name = "TRIGGER_GROUP", nullable = false, length = 200)
    private String triggerGroup;

    @Column(name = "CRON_EXPRESSION", nullable = false, length = 200)
    private String cronExpression;

    @Column(name = "TIME_ZONE_ID", length = 80)
    private String timeZoneId;

    @Column(name = "SCHED_NAME", nullable = false, length = 120)
    private String schedName;

    @Override
    public String toString() {
        return "{" +
                "triggerName='" + triggerName + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                '}';
    }
}

