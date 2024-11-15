package telsoft.scheduler.quartz.core.init;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import telsoft.scheduler.quartz.core.entity.ClassPath;
import telsoft.scheduler.quartz.core.repository.ClasspathRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Description("This class find all beans of type Job (Quartz) and save to database")
public class ClassPathFinder implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    Scheduler scheduler;

    @Autowired
    private ClasspathRepository classpathRepository;

    @Override
    public void run(String... args) throws Exception {
        // 1. Lấy tất cả các classpath của các bean kiểu Job từ ApplicationContext
        Set<String> scannedClasspaths = applicationContext.getBeansOfType(Job.class).values().stream()
                .map(job -> job.getClass().getName())
                .collect(Collectors.toSet());

        // 2. Lấy tất cả các classpath đang có trong database theo schedulerName
        Set<String> dbClassPaths = classpathRepository.findAllBySchedName(scheduler.getSchedulerName()).stream()
                .map(ClassPath::getClassPath)
                .collect(Collectors.toSet());

        // 3. Xóa các classpath không còn hiệu lực (có trong db nhưng không quét thấy)
        dbClassPaths.stream()
                .filter(classpath -> !scannedClasspaths.contains(classpath))
                .forEach(classpath -> classpathRepository.deleteById(classpath));

        // 4. Thêm các classpath mới vào database nếu chúng chưa có
        scannedClasspaths.stream()
                .filter(classpath -> !dbClassPaths.contains(classpath))
                .forEach(classpath -> {
                    try {
                        classpathRepository.save(new ClassPath(classpath, "Add description for new classpath.", scheduler.getSchedulerName()));
                    } catch (SchedulerException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}

