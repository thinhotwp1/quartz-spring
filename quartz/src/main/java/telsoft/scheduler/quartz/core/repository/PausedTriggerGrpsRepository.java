package telsoft.scheduler.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.core.entity.PausedTriggerGrp;

public interface PausedTriggerGrpsRepository extends JpaRepository<PausedTriggerGrp, String> {}
