package telsoft.scheduler.quartz.worker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.worker.core.entity.SimPropTrigger;

public interface SimpropTriggersRepository extends JpaRepository<SimPropTrigger, String> {}
