package telsoft.scheduler.quartz.core.service;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telsoft.scheduler.quartz.core.entity.Project;
import telsoft.scheduler.quartz.core.exception.NotFoundException;
import telsoft.scheduler.quartz.core.repository.*;
import telsoft.scheduler.quartz.core.repository.ProjectRepository;

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
