package com.task_management.modules.user.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateDTO {
    @NotBlank(message = "username is required.")
    private String username;

    @NotBlank(message = "password is required.")
    private String password;

    @NotBlank(message = "name is required.")
    private String name;

    private boolean admin;
}
