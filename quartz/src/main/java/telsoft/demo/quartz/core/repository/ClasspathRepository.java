package telsoft.demo.quartz.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telsoft.demo.quartz.core.entity.ClassPath;

@Repository
public interface ClasspathRepository extends JpaRepository<ClassPath, String> {
}
