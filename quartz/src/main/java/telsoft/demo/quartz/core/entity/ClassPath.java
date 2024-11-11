package telsoft.demo.quartz.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "QRTZ_CLASS_PATH")
@NoArgsConstructor
@AllArgsConstructor
public class ClassPath {
    @Id
    @Column(name = "CLASS_PATH", nullable = false)
    private String classPath;

    @Column(name = "DESCRIPTION")
    private String description;

}
