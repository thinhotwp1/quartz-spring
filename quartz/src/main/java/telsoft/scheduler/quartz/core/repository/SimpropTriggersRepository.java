package telsoft.scheduler.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.core.entity.SimPropTrigger;

public interface SimpropTriggersRepository extends JpaRepository<SimPropTrigger, String> {}
