package telsoft.scheduler.quartz.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.manager.entity.JobDetail;

public interface JobDetailRepository extends JpaRepository<JobDetail, String> {
}