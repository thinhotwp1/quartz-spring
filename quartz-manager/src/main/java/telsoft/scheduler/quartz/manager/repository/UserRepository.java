package telsoft.scheduler.quartz.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import telsoft.scheduler.quartz.manager.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
}
