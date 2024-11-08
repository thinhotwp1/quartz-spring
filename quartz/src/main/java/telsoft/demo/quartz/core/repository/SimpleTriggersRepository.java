package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.SimpleTriggers;

public interface SimpleTriggersRepository extends JpaRepository<SimpleTriggers, String> {}
