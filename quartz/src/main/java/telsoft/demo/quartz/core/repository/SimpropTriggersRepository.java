package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.SimPropTrigger;

public interface SimpropTriggersRepository extends JpaRepository<SimPropTrigger, String> {}
