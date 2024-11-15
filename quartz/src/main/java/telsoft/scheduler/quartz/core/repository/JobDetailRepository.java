package telsoft.scheduler.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.core.entity.JobDetail;

import java.util.Optional;

public interface JobDetailRepository extends JpaRepository<JobDetail, String> {
}