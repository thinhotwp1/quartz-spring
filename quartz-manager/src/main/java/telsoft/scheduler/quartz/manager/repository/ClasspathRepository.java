package telsoft.scheduler.quartz.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telsoft.scheduler.quartz.manager.entity.ClassPath;

import java.util.List;

@Repository
public interface ClasspathRepository extends JpaRepository<ClassPath, String> {
    List<ClassPath> findAllBySchedName(String schedName);
}
