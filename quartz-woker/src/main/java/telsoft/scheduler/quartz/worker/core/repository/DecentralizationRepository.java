package telsoft.scheduler.quartz.worker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.worker.core.entity.Decentralization;

public interface DecentralizationRepository extends JpaRepository<Decentralization, String> {
}