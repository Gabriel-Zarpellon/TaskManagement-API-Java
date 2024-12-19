package com.task_management.modules.task;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.task_management.exceptions.NotFoundException;
import com.task_management.modules.task.dtos.TaskCreateDTO;
import com.task_management.modules.user.UserEntity;
import com.task_management.modules.user.UserRepository;
import com.task_management.security.JwtTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    UserRepository userRepo;

    @PostMapping
    public ResponseEntity<TaskEntity> create(@Valid @RequestBody TaskCreateDTO payload, HttpServletRequest req) {
        String token = req.getHeader("Authorization");

        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }

        String username = jwtTokenService.extractUsername(token);
        UserEntity user = userRepo.findByUsername(username).get();
        //Long userId = user.getId();

        TaskEntity task = new TaskEntity();

        task.setTitle(payload.getTitle());
        task.setStatus(payload.getStatus());
        task.setDescription(payload.getDescription());
        task.setUser(user);

        TaskEntity response = taskService.create(task);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskEntity>> read(@RequestParam Optional<String> status) {
        List<TaskEntity> response = taskService.read(status);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskEntity> readOne(@PathVariable Long id) throws NotFoundException {
        TaskEntity response = taskService.readOne(id);

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws NotFoundException {
        taskService.delete(id);

        return ResponseEntity.status(204).body(null);
    }
}
