package telsoft.scheduler.quartz.manager.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import telsoft.scheduler.quartz.manager.entity.JobHistory;

import java.util.List;

public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {
    List<JobHistory> findAllByJobIdOrderByStartedAtAsc(String jobId);

    @Transactional
    @Modifying
    void deleteAllByJobId(String jobId);
}