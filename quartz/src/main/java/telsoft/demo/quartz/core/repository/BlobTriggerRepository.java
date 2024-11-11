package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.BlobTrigger;

public interface BlobTriggerRepository extends JpaRepository<BlobTrigger, String> {}
