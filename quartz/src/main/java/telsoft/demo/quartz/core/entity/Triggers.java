package telsoft.demo.quartz.core.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "QRTZ_TRIGGERS")
@Getter
@Setter
@NoArgsConstructor
public class Triggers {

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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "JOB_NAME", referencedColumnName = "JOB_NAME", insertable = false, updatable = false)
//    private JobDetails jobDetails;
//
//    @OneToMany(mappedBy = "schedName")
//    private Set<SimpleTriggers> schedNameSimpleTriggers;
//
//    @OneToMany(mappedBy = "schedName")
//    private Set<CronTriggers> schedNameCronTriggers;
//
//    @OneToMany(mappedBy = "schedName")
//    private Set<SimpropTriggers> schedNameSimpropTriggers;
//
//    @OneToMany(mappedBy = "schedName")
//    private Set<BlobTriggers> schedNameBlobTriggers;

}

