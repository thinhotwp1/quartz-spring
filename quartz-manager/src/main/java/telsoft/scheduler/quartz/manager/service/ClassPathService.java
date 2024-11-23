package telsoft.scheduler.quartz.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;
import telsoft.scheduler.quartz.manager.entity.ClassPath;
import telsoft.scheduler.quartz.manager.repository.ClasspathRepository;

import java.util.List;
import java.util.Optional;

@Service
@Description("This class using for find classpath in source code")
public class ClassPathService {
    @Autowired
    ClasspathRepository classpathRepository;

    public List<ClassPath> getAllClassPath(String schedName) {
        if (schedName != null && !schedName.isEmpty())
            return classpathRepository.findAllBySchedName(schedName);

        return classpathRepository.findAll();
    }

    public Optional<ClassPath> getClasspathById(String classpath) {
            return classpathRepository.findById(classpath);
    }
}
