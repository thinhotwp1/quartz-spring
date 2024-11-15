package telsoft.scheduler.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.core.entity.FiredTrigger;

public interface FiredTriggerRepository extends JpaRepository<FiredTrigger, String> {}
