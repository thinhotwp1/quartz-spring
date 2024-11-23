package telsoft.scheduler.quartz.worker.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "QRTZ_DECENTRALIZATION")
public class Decentralization extends BaseEntity {
    @Id
    @Column(name = "ROLE")
    private String role;

    @Column(name = "ACCESS")
    private String access;
}
