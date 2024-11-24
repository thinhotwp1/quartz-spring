package telsoft.scheduler.quartz.manager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import telsoft.scheduler.quartz.manager.exception.NotFoundException;
import telsoft.scheduler.quartz.manager.service.ProjectService;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllProject() throws  NotFoundException {
        return ResponseEntity.ok(projectService.getAllProject());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody String projectName) {
        projectService.createProject(projectName);
        return ResponseEntity.ok("Project created successfully: " + projectName);
    }
}

