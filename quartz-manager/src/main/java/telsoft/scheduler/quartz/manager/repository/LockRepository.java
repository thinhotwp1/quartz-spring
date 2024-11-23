package telsoft.scheduler.quartz.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.manager.entity.Lock;

public interface LockRepository extends JpaRepository<Lock, String> {}
