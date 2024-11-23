package telsoft.scheduler.quartz.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.manager.entity.JobHistory;

import java.util.List;

public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {
    List<JobHistory> findAllByJobIdOrderByStartedAtAsc(String jobId);
}