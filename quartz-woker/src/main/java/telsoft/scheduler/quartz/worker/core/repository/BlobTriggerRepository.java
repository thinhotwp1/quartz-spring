package telsoft.scheduler.quartz.worker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.worker.core.entity.BlobTrigger;

public interface BlobTriggerRepository extends JpaRepository<BlobTrigger, String> {}
