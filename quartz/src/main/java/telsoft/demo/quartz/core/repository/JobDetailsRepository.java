package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.demo.quartz.core.entity.JobDetails;

public interface JobDetailsRepository extends JpaRepository<JobDetails, String> {}