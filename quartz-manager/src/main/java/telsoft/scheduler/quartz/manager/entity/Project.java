package telsoft.scheduler.quartz.manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "QRTZ_PROJECT")
@Getter
@Setter
@NoArgsConstructor
public class Project extends BaseEntity {

    @Id
    @Column(name = "PROJECT_NAME", nullable = false, length = 200)
    private String projectName;

    @JsonIgnore
    @Column(name = "SCHED_NAME", nullable = false, length = 120)
    private String schedName;

}
