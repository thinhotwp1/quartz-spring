package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, String> {}
