package telsoft.demo.quartz.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "QRTZ_SIMPROP_TRIGGERS")
@Getter
@Setter
@NoArgsConstructor
public class SimPropTrigger {
    @Id
    @Column( name = "TRIGGER_NAME", nullable = false,length = 200)
    private String triggerName;

    @Column( name = "TRIGGER_GROUP", nullable = false, length = 200)
    private String triggerGroup;

    @Column(name = "STR_PROP_1", length = 512)
    private String strProp1;

    @Column(name = "STR_PROP_2", length = 512)
    private String strProp2;

    @Column(name = "STR_PROP_3", length = 512)
    private String strProp3;

    @Column(name = "INT_PROP_1")
    private Integer intProp1;

    @Column(name = "INT_PROP_2")
    private Integer intProp2;

    @Column(name = "LONG_PROP_1")
    private Long longProp1;

    @Column(name = "LONG_PROP_2")
    private Long longProp2;

    @Column(name = "DEC_PROP_1", precision = 17, scale = 4)
    private BigDecimal decProp1;

    @Column(name = "DEC_PROP_2", precision = 17, scale = 4)
    private BigDecimal decProp2;

    @Column(name = "BOOL_PROP_1", columnDefinition = "tinyint", length = 1)
    private Boolean boolProp1;

    @Column(name = "BOOL_PROP_2", columnDefinition = "tinyint", length = 1)
    private Boolean boolProp2;

    @Column(name = "SCHED_NAME", nullable = false, length = 120)
    private String schedName;

}

