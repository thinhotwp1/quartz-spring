package telsoft.scheduler.quartz.worker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.worker.core.entity.JobParam;

import java.util.List;

public interface JobParamRepository extends JpaRepository<JobParam, Long> {
    List<JobParam> findAllByJobName(String jobId);
}