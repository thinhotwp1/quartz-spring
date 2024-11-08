package telsoft.demo.quartz.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "QRTZ_SIMPLE_TRIGGERS")
@Getter
@Setter
@NoArgsConstructor
public class SimpleTriggers {

    @Id
    @Column(name = "TRIGGER_NAME", nullable = false, length = 200)
    private String triggerName;

    @Column(name = "TRIGGER_GROUP", nullable = false, length = 200)
    private String triggerGroup;

    @Column(name = "REPEAT_COUNT", nullable = false)
    private Long repeatCount;

    @Column(name = "REPEAT_INTERVAL", nullable = false)
    private Long repeatInterval;

    @Column(name = "TIMES_TRIGGERED", nullable = false)
    private Long timesTriggered;

    @Column(name = "SCHED_NAME", nullable = false, length = 120)
    private String schedName;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "SCHED_NAME", nullable = false)
//    private Triggers schedName;

}

