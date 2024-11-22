package telsoft.scheduler.quartz.core.entity;

import jakarta.persistence.*;
import lombok.*;
import telsoft.scheduler.quartz.core.enums.ParamType;

@Entity
@Table(name = "QRTZ_JOB_PARAM")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class JobParam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "JOB_NAME", nullable = false, length = 200)
    private String jobName;

    @Column(name = "PARAM_NAME", nullable = false, length = 200)
    private String paramName;

    @Column(name = "PARAM_TYPE", nullable = false)
    private ParamType paramType;

    @Column(name = "PARAM_VALUE")
    private String paramValue;

}
