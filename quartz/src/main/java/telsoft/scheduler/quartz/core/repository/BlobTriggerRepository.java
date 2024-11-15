package telsoft.scheduler.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.core.entity.BlobTrigger;

public interface BlobTriggerRepository extends JpaRepository<BlobTrigger, String> {}
