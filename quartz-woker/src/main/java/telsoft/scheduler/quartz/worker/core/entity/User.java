package telsoft.scheduler.quartz.worker.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "QRTZ_USER")
public class User extends BaseEntity {
    @Id
    @Column(name = "USR_NAME")
    private String userName;

    @JsonIgnore
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ROLE")
    private String role;

}
