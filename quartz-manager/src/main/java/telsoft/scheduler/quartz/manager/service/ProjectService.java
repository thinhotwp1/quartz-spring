package telsoft.scheduler.quartz.manager.service;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telsoft.scheduler.quartz.manager.entity.Project;
import telsoft.scheduler.quartz.manager.exception.NotFoundException;
import telsoft.scheduler.quartz.manager.repository.ProjectRepository;

import java.util.*;

@Service
public class ProjectService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    ProjectRepository projectRepository;

    public List<Project> getAllProject() throws SchedulerException, NotFoundException {
        return projectRepository.findAllBySchedName(scheduler.getSchedulerName());
    }

    public void createProject(String projectName) throws SchedulerException {
        Project project = new Project();
        project.setProjectName(projectName);
        project.setSchedName(scheduler.getSchedulerName());
        projectRepository.saveAndFlush(project);
    }

    public List<Project> getProjectByName(String projectName) {
        return projectRepository.findAllById(Collections.singleton(projectName));
    }
}
