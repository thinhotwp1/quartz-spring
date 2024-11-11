package telsoft.demo.quartz.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import telsoft.demo.quartz.core.service.ClassPathService;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("/api/class-path")
public class ClassPathController {

    @Autowired
    ClassPathService classPathService;

    @GetMapping("/get-all-classpath")
    public ResponseEntity<?> getAllClassPath(){
        return ResponseEntity.ok(classPathService.getAllClassPath());
    }
}
