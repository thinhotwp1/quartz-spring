package telsoft.demo.quartz.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "QRTZ_CRON_TRIGGERS")
@Getter
@Setter
@NoArgsConstructor
public class CronTriggers {

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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "SCHED_NAME", nullable = false)
//    private Triggers schedName;
}

