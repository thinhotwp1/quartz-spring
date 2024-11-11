package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.Trigger;

import java.util.Set;

public interface TriggerRepository extends JpaRepository<Trigger, String> {
    Set<Trigger> findTriggersByJobName(String jobName);
}
