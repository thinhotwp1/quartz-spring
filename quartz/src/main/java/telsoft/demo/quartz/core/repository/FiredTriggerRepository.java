package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.FiredTrigger;

public interface FiredTriggerRepository extends JpaRepository<FiredTrigger, String> {}
