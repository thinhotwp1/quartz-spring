package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.SchedulerState;

public interface SchedulerStateRepository extends JpaRepository<SchedulerState, String> {}
