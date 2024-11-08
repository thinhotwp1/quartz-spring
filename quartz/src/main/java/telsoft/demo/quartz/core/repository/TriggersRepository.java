package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.Triggers;

import java.util.List;
import java.util.Set;

public interface TriggersRepository extends JpaRepository<Triggers, String> {
    Set<Triggers> findTriggersByJobName(String jobName);
}
