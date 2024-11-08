package telsoft.demo.quartz.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telsoft.demo.quartz.core.entity.ClassPath;
import telsoft.demo.quartz.core.repository.ClasspathRepository;

import java.util.List;

@Service
public class ClassPathService {
    @Autowired
    ClasspathRepository classpathRepository;

    public List<ClassPath> getAllClassPath() {
        return classpathRepository.findAll();
    }
}
