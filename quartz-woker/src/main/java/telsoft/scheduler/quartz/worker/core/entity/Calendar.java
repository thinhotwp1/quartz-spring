package telsoft.scheduler.quartz.worker.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "QRTZ_CALENDARS")
@Getter
@Setter
public class Calendar extends BaseEntity {
    @Id
    @Column(name = "CALENDAR_NAME", nullable = false, length = 200)
    private String calendarName;

    @Column(name = "SCHED_NAME", nullable = false,  length = 120)
    private String schedName;

    @Column(name = "CALENDAR", nullable = false, columnDefinition = "longtext")
    private String calendar;

}

