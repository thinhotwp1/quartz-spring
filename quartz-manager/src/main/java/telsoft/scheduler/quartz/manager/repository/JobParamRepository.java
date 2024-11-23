package telsoft.scheduler.quartz.manager.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.manager.entity.JobParam;

import java.util.List;

public interface JobParamRepository extends JpaRepository<JobParam, Long> {
    List<JobParam> findAllByJobName(String jobId);

    @Transactional
    void deleteJobParamsByJobName(String jobId);
}