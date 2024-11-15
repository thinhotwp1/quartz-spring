package telsoft.scheduler.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.core.entity.SchedulerState;

public interface SchedulerStateRepository extends JpaRepository<SchedulerState, String> {}
