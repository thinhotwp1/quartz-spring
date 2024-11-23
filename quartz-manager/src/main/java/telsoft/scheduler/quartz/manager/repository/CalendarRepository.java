package telsoft.scheduler.quartz.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.manager.entity.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, String> {}
