package telsoft.scheduler.quartz.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.manager.entity.Decentralization;

public interface DecentralizationRepository extends JpaRepository<Decentralization, String> {
}