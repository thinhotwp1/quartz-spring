package telsoft.demo.quartz.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "QRTZ_PAUSED_TRIGGER_GRPS")
@Getter
@Setter
@NoArgsConstructor
public class PausedTriggerGrps {

    @Id
    @Column(name = "SCHED_NAME", nullable = false, length = 120)
    private String schedName;

    @Column(name = "TRIGGER_GROUP", nullable = false, length = 200)
    private String triggerGroup;

}

