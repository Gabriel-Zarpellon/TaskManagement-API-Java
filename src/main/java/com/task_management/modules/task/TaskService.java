package com.task_management.modules.task;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.task_management.exceptions.NotFoundException;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepo;

    public TaskEntity create(TaskEntity payload) {
        return taskRepo.save(payload);
    }

    public List<TaskEntity> read(Optional<String> status) {
        if (status.isEmpty()) {
            return taskRepo.findAll();
        }
        return taskRepo.findByStatus(status);
    }

    public TaskEntity readOne(Long id) throws NotFoundException {
        return taskRepo.findById(id).orElseThrow(() -> new NotFoundException());
    }

    public void delete(Long id) throws NotFoundException {
        taskRepo.findById(id).orElseThrow(() -> new NotFoundException());
        taskRepo.deleteById(id);
        return;
    }
}
