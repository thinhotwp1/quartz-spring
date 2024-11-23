package telsoft.scheduler.quartz.worker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.worker.core.entity.PausedTriggerGrp;

public interface PausedTriggerGrpsRepository extends JpaRepository<PausedTriggerGrp, String> {}
