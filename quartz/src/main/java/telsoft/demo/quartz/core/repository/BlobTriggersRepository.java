package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.BlobTriggers;

public interface BlobTriggersRepository extends JpaRepository<BlobTriggers, String> {}
