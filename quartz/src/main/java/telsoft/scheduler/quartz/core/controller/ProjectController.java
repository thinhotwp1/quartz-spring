package telsoft.scheduler.quartz.core.controller;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import telsoft.scheduler.quartz.core.entity.Project;
import telsoft.scheduler.quartz.core.exception.NotFoundException;
import telsoft.scheduler.quartz.core.service.ProjectService;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllProject() throws SchedulerException, NotFoundException {
        return ResponseEntity.ok(projectService.getAllProject());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody String projectName) throws SchedulerException{
        projectService.createProject(projectName);
        return ResponseEntity.ok("Project created successfully: " + projectName);
    }
}

