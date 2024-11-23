package telsoft.scheduler.quartz.worker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.worker.core.entity.SimpleTrigger;

public interface SimpleTriggerRepository extends JpaRepository<SimpleTrigger, String> {
    SimpleTrigger findByTriggerName(String triggerName);
}
