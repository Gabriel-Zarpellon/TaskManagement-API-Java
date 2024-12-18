package com.task_management.modules.user.dtos;

import lombok.Data;

@Data
public class UserReturnDTO {
    private String id;
    private String username;
    private String name;
    private boolean isAdmin;
}
