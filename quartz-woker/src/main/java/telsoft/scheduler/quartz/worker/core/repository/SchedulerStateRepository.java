package telsoft.scheduler.quartz.worker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.worker.core.entity.SchedulerState;

public interface SchedulerStateRepository extends JpaRepository<SchedulerState, String> {}
