package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.CronTriggers;

public interface CronTriggersRepository extends JpaRepository<CronTriggers, String> {}
