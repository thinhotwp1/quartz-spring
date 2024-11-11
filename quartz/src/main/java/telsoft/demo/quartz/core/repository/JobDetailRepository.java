package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.JobDetail;

import java.util.Optional;

public interface JobDetailRepository extends JpaRepository<JobDetail, String> {
    Optional<JobDetail> findByJobNameAndJobGroup(String jobName, String jobGroup);
}