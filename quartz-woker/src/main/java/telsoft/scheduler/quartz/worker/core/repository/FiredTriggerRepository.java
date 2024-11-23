package telsoft.scheduler.quartz.worker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.worker.core.entity.FiredTrigger;

public interface FiredTriggerRepository extends JpaRepository<FiredTrigger, String> {}
