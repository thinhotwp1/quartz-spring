package telsoft.scheduler.quartz.worker.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "QRTZ_SIMPLE_TRIGGERS")
@Getter
@Setter
@NoArgsConstructor
public class SimpleTrigger extends BaseEntity {
    @Id
    @Column(name = "TRIGGER_NAME", nullable = false, length = 200)
    private String triggerName;

    @Column(name = "TRIGGER_GROUP", nullable = false, length = 200)
    private String triggerGroup;

    @Column(name = "REPEAT_COUNT", nullable = false)
    private Long repeatCount;

    @Column(name = "REPEAT_INTERVAL", nullable = false)
    private Long repeatInterval;

    @Column(name = "TIMES_TRIGGERED", nullable = false)
    private Long timesTriggered;

    @Column(name = "SCHED_NAME", nullable = false, length = 120)
    private String schedName;

    @Override
    public String toString() {
        return "{" +
                "triggerName:'" + triggerName + '\'' +
                ", triggerGroup:'" + triggerGroup + '\'' +
                ", repeatCount:" + repeatCount +
                ", repeatInterval:" + repeatInterval +
                ", timesTriggered:" + timesTriggered +
                ", schedName:'" + schedName + '\'' +
                '}';
    }
}

