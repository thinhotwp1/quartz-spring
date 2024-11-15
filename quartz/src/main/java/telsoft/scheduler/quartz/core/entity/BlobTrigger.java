package telsoft.scheduler.quartz.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "QRTZ_BLOB_TRIGGERS")
@Getter
@Setter
@NoArgsConstructor
public class BlobTrigger extends BaseEntity {
    @Id
    @Column(name = "TRIGGER_NAME", nullable = false, length = 200)
    private String triggerName;

    @Column(name = "TRIGGER_GROUP", nullable = false, length = 200)
    private String triggerGroup;

    @Column(name = "BLOB_DATA", columnDefinition = "longtext")
    @Lob
    private String blobData;

    @Column(name = "SCHED_NAME", nullable = false, length = 120)
    private String schedName;

}

