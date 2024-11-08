package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.FiredTriggers;

public interface FiredTriggersRepository extends JpaRepository<FiredTriggers, String> {}
