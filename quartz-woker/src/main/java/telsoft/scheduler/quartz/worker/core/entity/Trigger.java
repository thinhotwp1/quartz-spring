package telsoft.scheduler.quartz.worker.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "QRTZ_TRIGGERS")
@Getter
@Setter
@NoArgsConstructor
public class Trigger extends BaseEntity {
    @Id
    @Column(name = "TRIGGER_NAME", nullable = false, length = 200)
    private String triggerName;

    @Column(name = "TRIGGER_GROUP", nullable = false, length = 200)
    private String triggerGroup;

    @Column(name = "JOB_NAME", nullable = false, length = 200)
    private String jobName;

    @Column(name = "JOB_GROUP", nullable = false, length = 200)
    private String jobGroup;

    @Column(name = "DESCRIPTION", length = 250)
    private String description;

    @Column(name = "NEXT_FIRE_TIME")
    private Long nextFireTime;

    @Column(name = "PREV_FIRE_TIME")
    private Long prevFireTime;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "TRIGGER_STATE", nullable = false, length = 16)
    private String triggerState;

    @Column(name = "TRIGGER_TYPE", nullable = false, length = 8)
    private String triggerType;

    @Column(name = "START_TIME", nullable = false)
    private Long startTime;

    @Column(name = "END_TIME")
    private Long endTime;

    @Column(name = "CALENDAR_NAME", length = 200)
    private String calendarName;

    @Column(name = "MISFIRE_INSTR")
    private Integer misfireInstr;

    @Column(name = "JOB_DATA", columnDefinition = "longtext")
    private String jobData;

    @Column(name = "SCHED_NAME", nullable = false, length = 120)
    private String schedName;

    @Transient
    private SimpleTrigger simpleTriggers;

    @Transient
    private Set<CronTrigger> cronTriggers;

    @Override
    public String toString() {
        return "{" +
                "triggerName:'" + triggerName + '\'' +
                ", triggerGroup:'" + triggerGroup + '\'' +
                ", jobName:'" + jobName + '\'' +
                ", jobGroup:'" + jobGroup + '\'' +
                ", description:'" + description + '\'' +
                ", nextFireTime:" + nextFireTime +
                ", prevFireTime:" + prevFireTime +
                ", priority:" + priority +
                ", triggerState:'" + triggerState + '\'' +
                ", triggerType:'" + triggerType + '\'' +
                ", startTime:" + startTime +
                ", endTime:" + endTime +
                ", calendarName:'" + calendarName + '\'' +
                ", misfireInstr:" + misfireInstr +
                ", jobData:'" + jobData + '\'' +
                ", schedName:'" + schedName + '\'' +
                ", simpleTriggers:" + simpleTriggers +
                ", cronTriggers:" + cronTriggers +
                '}';
    }
}

