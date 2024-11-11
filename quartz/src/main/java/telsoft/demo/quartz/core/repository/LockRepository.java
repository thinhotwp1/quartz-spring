package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.Lock;

public interface LockRepository extends JpaRepository<Lock, String> {}
