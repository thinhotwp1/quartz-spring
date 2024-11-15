package telsoft.scheduler.quartz.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import telsoft.scheduler.quartz.core.service.ClassPathService;

@RestController
@RequestMapping("/class-path")
public class ClassPathController {

    @Autowired
    ClassPathService classPathService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllClassPath(@RequestParam(required = false) String schedName) {
        return ResponseEntity.ok(classPathService.getAllClassPath(schedName));
    }
}
