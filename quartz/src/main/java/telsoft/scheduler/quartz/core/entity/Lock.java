package telsoft.scheduler.quartz.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "QRTZ_LOCKS")
@Getter
@Setter
@NoArgsConstructor
public class Lock extends BaseEntity {
    @Id
    @Column(name = "SCHED_NAME", nullable = false, length = 120)
    private String schedName;

    @Column(name = "LOCK_NAME", nullable = false, length = 40)
    private String lockName;

}

