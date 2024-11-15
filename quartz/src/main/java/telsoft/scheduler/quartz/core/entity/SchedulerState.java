package telsoft.scheduler.quartz.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "QRTZ_SCHEDULER_STATE")
@Getter
@Setter
@NoArgsConstructor
public class SchedulerState extends BaseEntity {
    @Id
    @Column(name = "INSTANCE_NAME", nullable = false, length = 200)
    private String instanceName;

    @Column(name = "SCHED_NAME", nullable = false, updatable = false, length = 120)
    private String schedName;

    @Column(name = "IP", length = 120)
    private String ip;

    @Column(name = "LAST_CHECKIN_TIME", nullable = false)
    private Long lastCheckinTime;

    @Column(name = "CHECKIN_INTERVAL", nullable = false)
    private Long checkinInterval;

}

