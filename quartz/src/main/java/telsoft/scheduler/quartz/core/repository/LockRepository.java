package telsoft.scheduler.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.core.entity.Lock;

public interface LockRepository extends JpaRepository<Lock, String> {}
