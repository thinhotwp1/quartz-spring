package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.SimpleTrigger;

public interface SimpleTriggerRepository extends JpaRepository<SimpleTrigger, String> {}
