package com.task_management.modules.user.dtos;

import java.util.List;

import com.task_management.modules.task.TaskEntity;

import lombok.Data;

@Data
public class UserReturnDTO {
    private String id;
    private String username;
    private String name;
    private boolean isAdmin;
    private List<TaskEntity> tasks;
}
