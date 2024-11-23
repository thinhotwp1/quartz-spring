package telsoft.scheduler.quartz.worker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.worker.core.entity.JobDetail;

public interface JobDetailRepository extends JpaRepository<JobDetail, String> {
}