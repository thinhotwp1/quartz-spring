package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.CronTrigger;

public interface CronTriggerRepository extends JpaRepository<CronTrigger, String> {
}