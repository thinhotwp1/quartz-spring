package telsoft.demo.quartz.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "QRTZ_FIRED_TRIGGERS")
@Getter
@Setter
@NoArgsConstructor
public class FiredTrigger {
    @Id
    @Column(name = "ENTRY_ID", nullable = false, length = 95)
    private String entryId;

    @Column(name = "SCHED_NAME", nullable = false, length = 120)
    private String schedName;

    @Column(name = "TRIGGER_NAME", nullable = false, length = 200)
    private String triggerName;

    @Column(name = "TRIGGER_GROUP", nullable = false, length = 200)
    private String triggerGroup;

    @Column(name = "INSTANCE_NAME", nullable = false, length = 200)
    private String instanceName;

    @Column(name = "FIRED_TIME", nullable = false)
    private Long firedTime;

    @Column(name = "SCHED_TIME", nullable = false)
    private Long schedTime;

    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    @Column(name = "STATE", nullable = false, length = 16)
    private String state;

    @Column(name = "JOB_NAME", length = 200)
    private String jobName;

    @Column(name = "JOB_GROUP", length = 200)
    private String jobGroup;

    @Column(name = "IS_NONCONCURRENT", columnDefinition = "tinyint", length = 1)
    private Boolean isNonconcurrent;

    @Column(name = "REQUESTS_RECOVERY", columnDefinition = "tinyint", length = 1)
    private Boolean requestsRecovery;
}

