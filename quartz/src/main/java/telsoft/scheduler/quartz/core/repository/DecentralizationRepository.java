package telsoft.scheduler.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.core.entity.Decentralization;

public interface DecentralizationRepository extends JpaRepository<Decentralization, String> {
}