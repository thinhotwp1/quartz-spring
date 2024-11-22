package telsoft.scheduler.quartz.core.repository;

import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import telsoft.scheduler.quartz.core.entity.JobHistory;

import java.util.List;

public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {
    List<JobHistory> findAllByJobIdOrderByStartedAtAsc(String jobId);
}