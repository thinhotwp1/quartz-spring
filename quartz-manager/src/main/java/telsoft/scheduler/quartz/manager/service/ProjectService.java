package telsoft.scheduler.quartz.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import telsoft.scheduler.quartz.manager.entity.Project;
import telsoft.scheduler.quartz.manager.exception.NotFoundException;
import telsoft.scheduler.quartz.manager.repository.ProjectRepository;

import java.util.*;

@Service
public class ProjectService {

    @Value("${quartz.scheduler-name}")
    private String schedulerName;

    @Autowired
    ProjectRepository projectRepository;

    public List<Project> getAllProject() throws  NotFoundException {
        return projectRepository.findAllBySchedName(schedulerName);
    }

    public void createProject(String projectName)  {
        Project project = new Project();
        project.setProjectName(projectName);
        project.setSchedName(schedulerName);
        projectRepository.saveAndFlush(project);
    }

    public List<Project> getProjectByName(String projectName) {
        return projectRepository.findAllById(Collections.singleton(projectName));
    }
}
