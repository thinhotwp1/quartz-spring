package telsoft.scheduler.quartz.worker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.worker.core.entity.CronTrigger;

import java.util.Set;

public interface CronTriggerRepository extends JpaRepository<CronTrigger, String> {
    Set<CronTrigger> findAllByTriggerName(String triggerName);
}