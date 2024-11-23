package telsoft.scheduler.quartz.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.manager.entity.SimPropTrigger;

public interface SimpropTriggersRepository extends JpaRepository<SimPropTrigger, String> {}
