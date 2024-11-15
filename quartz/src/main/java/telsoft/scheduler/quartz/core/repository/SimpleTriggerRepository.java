package telsoft.scheduler.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.core.entity.SimpleTrigger;

public interface SimpleTriggerRepository extends JpaRepository<SimpleTrigger, String> {
    SimpleTrigger findByTriggerName(String triggerName);
}
