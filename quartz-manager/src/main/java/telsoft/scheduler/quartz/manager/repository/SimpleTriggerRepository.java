package telsoft.scheduler.quartz.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.manager.entity.SimpleTrigger;

public interface SimpleTriggerRepository extends JpaRepository<SimpleTrigger, String> {
    SimpleTrigger findByTriggerName(String triggerName);
}
