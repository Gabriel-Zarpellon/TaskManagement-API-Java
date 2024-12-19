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
import com.task_management.exceptions.NotOwnerException;
import com.task_management.modules.task.dtos.TaskCreateDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskEntity> create(@Valid @RequestBody TaskCreateDTO payload, HttpServletRequest req) {
        TaskEntity response = taskService.create(payload, req);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskEntity>> read(HttpServletRequest req, @RequestParam Optional<String> status) {
        List<TaskEntity> response = taskService.read(req, status);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskEntity> readOne(@PathVariable Long id, HttpServletRequest req)
            throws NotFoundException, NotOwnerException {
        TaskEntity response = taskService.readOne(id, req);

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest req)
            throws NotFoundException, NotOwnerException {
        taskService.delete(id, req);

        return ResponseEntity.status(204).body(null);
    }
}
