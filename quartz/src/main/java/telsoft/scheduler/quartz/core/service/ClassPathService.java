package telsoft.scheduler.quartz.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telsoft.scheduler.quartz.core.entity.ClassPath;
import telsoft.scheduler.quartz.core.repository.ClasspathRepository;

import java.util.List;

@Service
public class ClassPathService {
    @Autowired
    ClasspathRepository classpathRepository;

    public List<ClassPath> getAllClassPath(String schedName) {
        if (schedName != null && !schedName.isEmpty())
            return classpathRepository.findAllBySchedName(schedName);

        return classpathRepository.findAll();
    }
}
