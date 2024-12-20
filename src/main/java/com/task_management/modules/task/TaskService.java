package com.task_management.modules.task;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.task_management.exceptions.NotFoundException;
import com.task_management.exceptions.NotOwnerException;
import com.task_management.modules.task.dtos.TaskCreateDTO;
import com.task_management.modules.user.UserEntity;
import com.task_management.modules.user.UserRepository;
import com.task_management.security.JwtTokenService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    UserRepository userRepo;

    public TaskEntity create(TaskCreateDTO payload, HttpServletRequest req) {
        UserEntity user = jwtTokenService.getUser(req);

        TaskEntity task = new TaskEntity();

        task.setTitle(payload.getTitle());
        task.setStatus(payload.getStatus());
        task.setDescription(payload.getDescription());
        task.setUser(user);

        return taskRepo.save(task);
    }

    public List<TaskEntity> read(HttpServletRequest req, Optional<String> status) {
        UserEntity user = jwtTokenService.getUser(req);

        if (user.getAdmin()) {
            if (status.isEmpty()) {
                return taskRepo.findAll();
            }
            return taskRepo.findByStatus(status);
        }

        if (status.isEmpty()) {
            List<TaskEntity> tasks = taskRepo.findAll();

            var stream = tasks.stream();

            return stream.filter(t -> t.getId() == user.getId()).toList();
        }

        List<TaskEntity> tasks = taskRepo.findByStatus(status);
        var stream = tasks.stream();

        return stream.filter(t -> t.getId() == user.getId()).toList();
    }

    public TaskEntity readOne(Long id, HttpServletRequest req) throws NotFoundException, NotOwnerException {
        UserEntity user = jwtTokenService.getUser(req);

        TaskEntity task = taskRepo.findById(id).orElseThrow(() -> new NotFoundException());
        var ownerId = task.getUser().getId();

        if (ownerId != user.getId() && !user.getAdmin()) {
            throw new NotOwnerException();
        }
        return task;
    }

    public TaskEntity update(Long id, HttpServletRequest req, TaskEntity payload)
            throws NotFoundException, NotOwnerException {
        UserEntity user = jwtTokenService.getUser(req);

        TaskEntity task = taskRepo.findById(id).orElseThrow(() -> new NotFoundException());
        var ownerId = task.getUser().getId();

        if (ownerId != user.getId() && !user.getAdmin()) {
            throw new NotOwnerException();
        }

        task.setStatus(payload.getStatus() == null ? task.getStatus() : payload.getStatus());
        task.setTitle(payload.getTitle() == null ? task.getTitle() : payload.getTitle());
        task.setDescription(payload.getDescription() == null ? task.getDescription() : payload.getDescription());

        return task;
    }

    public void delete(Long id, HttpServletRequest req) throws NotFoundException, NotOwnerException {
        UserEntity user = jwtTokenService.getUser(req);

        TaskEntity task = taskRepo.findById(id).orElseThrow(() -> new NotFoundException());
        var ownerId = task.getUser().getId();

        if (ownerId != user.getId() && !user.getAdmin()) {
            throw new NotOwnerException();
        }

        taskRepo.deleteById(id);
        return;
    }
}
