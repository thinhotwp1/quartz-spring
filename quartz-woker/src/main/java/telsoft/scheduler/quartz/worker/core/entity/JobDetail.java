package telsoft.scheduler.quartz.worker.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "QRTZ_JOB_DETAILS")
@Getter
@Setter
@NoArgsConstructor
public class JobDetail extends BaseEntity {
    @Id
    @Column(name = "JOB_NAME", nullable = false, length = 200)
    private String jobName;

    @Column(name = "JOB_ALIAS", length = 200)
    private String jobAlias;

    @Column(name = "PROJECT_NAME", length = 200)
    private String projectName;

    @Column(name = "SCHED_NAME", nullable = false)
    private String schedName;

    @Column(name = "JOB_GROUP", nullable = false, length = 200)
    private String jobGroup;

    @Column(name = "DESCRIPTION", length = 250)
    private String description;

    @Column(name = "JOB_CLASS_NAME", nullable = false, length = 250)
    private String jobClassName;

    @Column(name = "IS_DURABLE", nullable = false)
    private Boolean isDurable;

    @Column(name = "IS_DEBUG")
    private Boolean isDebug;

    @Column(name = "IS_NONCONCURRENT", nullable = false)
    private Boolean isNonconcurrent;

    @Column(name = "IS_UPDATE_DATA", nullable = false)
    private Boolean isUpdateData;

    @Column(name = "IS_DISABLE")
    private Boolean isDisable;

    @Column(name = "REQUESTS_RECOVERY", nullable = false)
    private Boolean requestsRecovery;

    @JsonIgnore
    @Column(name = "JOB_DATA", columnDefinition = "longtext")
    private String jobData;

    @Transient
    private List<JobParam> jobParams;

    @Transient
    private Set<Trigger> triggers;

}

