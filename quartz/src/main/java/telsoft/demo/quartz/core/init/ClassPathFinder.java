package telsoft.demo.quartz.core.init;

import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import telsoft.demo.quartz.core.entity.ClassPath;
import telsoft.demo.quartz.core.repository.ClasspathRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Description("This class find all beans of type Job (Quartz) and save to database")
public class ClassPathFinder implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ClassPathFinder.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ClasspathRepository classpathRepository;

    @Override
    public void run(String... args) {
        // 1. Lấy tất cả các classpath của các bean kiểu Job từ ApplicationContext
        Set<String> scannedClasspaths = applicationContext.getBeansOfType(Job.class).values().stream()
                .map(job -> job.getClass().getName())
                .collect(Collectors.toSet());

        // 2. Lấy tất cả các classpath đang có trong database
        Set<String> dbClassPaths = classpathRepository.findAll().stream()
                .map(ClassPath::getClassPath)
                .collect(Collectors.toSet());

        // 3. Xóa các classpath không còn hiệu lực (có trong db nhưng không quét thấy)
        dbClassPaths.stream()
                .filter(classpath -> !scannedClasspaths.contains(classpath))
                .forEach(classpath -> classpathRepository.deleteById(classpath));

        // 4. Thêm các classpath mới vào database nếu chúng chưa có
        scannedClasspaths.stream()
                .filter(classpath -> !dbClassPaths.contains(classpath))
                .forEach(classpath -> classpathRepository.save(new ClassPath(classpath, "Add description for new classpath.")));
    }
}

