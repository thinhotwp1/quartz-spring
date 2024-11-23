package telsoft.scheduler.quartz.worker.core.entity;

import jakarta.persistence.*;
import lombok.*;
import telsoft.scheduler.quartz.worker.core.enums.StartType;

import java.util.Date;

@Entity
@Table(name = "QRTZ_JOB_HISTORY", indexes = @Index(name = "job_id_index", columnList = "JOB_ID"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "JOB_ID", nullable = false, length = 200)
    private String jobId;

    @Column(name = "STARTED_AT", nullable = false, length = 200)
    private Date startedAt;

    @Column(name = "FINISHED_AT", nullable = false, length = 200)
    private Date finishedAt;

    @Column(name = "DURATION", nullable = false, length = 200)
    private long duration;

    @Column(name = "JOB_STATUS", nullable = false, length = 200)
    private String jobStatus;

    @Column(name = "JOB_DATA")
    private String jobData;

    @Column(name = "START_TYPE", length = 200)
    private StartType startType;

    @Column(name = "JOB_TRIGGER", nullable = false)
    private String jobTrigger;

    @Column(name = "LOG", columnDefinition = "longtext")
    @Lob
    private String log;

    @Column(name = "THREAD_NAME", length = 200)
    private String threadName;

    @Column(name = "INSTANCE_NAME", length = 200)
    private String instanceName;

    @Column(name = "ERROR")
    private String error;

    @Column(name = "JVM_ID", length = 200)
    private String jvmId;

}

