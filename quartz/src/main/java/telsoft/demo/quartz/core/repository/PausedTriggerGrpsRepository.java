package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.PausedTriggerGrps;

public interface PausedTriggerGrpsRepository extends JpaRepository<PausedTriggerGrps, String> {}
