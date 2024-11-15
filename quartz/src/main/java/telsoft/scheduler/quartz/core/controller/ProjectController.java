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
//    @GetMapping("/get-by-name")
//    public ResponseEntity<?> getProjectByName(@RequestParam String projectName) throws NotFoundException {
//        return ResponseEntity.ok(projectService.getProjectByName(projectName));
//    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody Project project) throws SchedulerException{
        projectService.createProject(project);
        return ResponseEntity.ok("Project created successfully: " + project.getProjectName());
    }
}

