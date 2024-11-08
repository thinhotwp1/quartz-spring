package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.Locks;

public interface LocksRepository extends JpaRepository<Locks, String> {}
