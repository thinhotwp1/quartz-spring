package telsoft.scheduler.quartz.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.manager.entity.PausedTriggerGrp;

public interface PausedTriggerGrpsRepository extends JpaRepository<PausedTriggerGrp, String> {}
