package telsoft.demo.quartz.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "QRTZ_JOB_DETAILS")
@Getter
@Setter
@NoArgsConstructor
public class JobDetails {
    @Id
    @Column(name = "JOB_NAME", nullable = false, length = 200)
    private String jobName;

    @Column(name = "SCHED_NAME", nullable = false, length = 120)
    private String schedName;

    @Column(name = "JOB_GROUP", nullable = false, length = 200)
    private String jobGroup;

    @Column(name = "DESCRIPTION", length = 250)
    private String description;

    @Column(name = "JOB_CLASS_NAME", nullable = false, length = 250)
    private String jobClassName;

    @Column(name = "IS_DURABLE", nullable = false, columnDefinition = "tinyint", length = 1)
    private Boolean isDurable;

    @Column(name = "IS_NONCONCURRENT", nullable = false, columnDefinition = "tinyint", length = 1)
    private Boolean isNonconcurrent;

    @Column(name = "IS_UPDATE_DATA", nullable = false, columnDefinition = "tinyint", length = 1)
    private Boolean isUpdateData;

    @Column(name = "REQUESTS_RECOVERY", nullable = false, columnDefinition = "tinyint", length = 1)
    private Boolean requestsRecovery;

    @JsonIgnore(value = true)
    @Column(name = "JOB_DATA", columnDefinition = "longtext")
    private String jobData;

    @Transient
    private Map<String,Object> jobDataMap;

    @Transient
    private Set<Triggers> triggers;

//    @OneToMany(mappedBy = "jobDetails")
//    private Set<Triggers> triggers;
}

