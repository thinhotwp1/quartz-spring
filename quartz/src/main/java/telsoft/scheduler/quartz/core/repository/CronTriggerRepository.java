package telsoft.scheduler.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.core.entity.CronTrigger;

import java.util.Set;

public interface CronTriggerRepository extends JpaRepository<CronTrigger, String> {
    Set<CronTrigger> findAllByTriggerName(String triggerName);
}