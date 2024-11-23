package telsoft.scheduler.quartz.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.manager.entity.SchedulerState;

public interface SchedulerStateRepository extends JpaRepository<SchedulerState, String> {}
