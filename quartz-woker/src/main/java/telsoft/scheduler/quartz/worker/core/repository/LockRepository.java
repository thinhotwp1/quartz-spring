package telsoft.scheduler.quartz.worker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.worker.core.entity.Lock;

public interface LockRepository extends JpaRepository<Lock, String> {}
