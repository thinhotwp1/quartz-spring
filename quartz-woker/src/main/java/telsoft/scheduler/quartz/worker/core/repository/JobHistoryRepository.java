package telsoft.scheduler.quartz.worker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.worker.core.entity.JobHistory;

import java.util.List;

public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {
    List<JobHistory> findAllByJobIdOrderByStartedAtAsc(String jobId);
}