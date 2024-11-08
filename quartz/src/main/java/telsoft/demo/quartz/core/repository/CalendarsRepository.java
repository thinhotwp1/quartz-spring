package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.Calendars;

public interface CalendarsRepository extends JpaRepository<Calendars, String> {}
