package telsoft.scheduler.quartz.manager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "QRTZ_CLASS_PATH")
@NoArgsConstructor
@AllArgsConstructor
public class ClassPath extends BaseEntity {
    @Id
    @Column(name = "CLASS_PATH", nullable = false)
    private String classPath;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SCHED_NAME", nullable = false, length = 120)
    private String schedName;

}
