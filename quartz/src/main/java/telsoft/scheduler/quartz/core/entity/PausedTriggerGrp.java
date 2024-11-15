package telsoft.scheduler.quartz.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "QRTZ_PAUSED_TRIGGER_GRPS")
@Getter
@Setter
@NoArgsConstructor
public class PausedTriggerGrp extends BaseEntity {
    @Id
    @Column(name = "SCHED_NAME", nullable = false, length = 120)
    private String schedName;

    @Column(name = "TRIGGER_GROUP", nullable = false, length = 200)
    private String triggerGroup;

}

