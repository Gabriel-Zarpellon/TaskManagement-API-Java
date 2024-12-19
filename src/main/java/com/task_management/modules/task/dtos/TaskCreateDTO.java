package com.task_management.modules.task.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskCreateDTO {
    @NotBlank(message = "Title is required.")
    private String title;

    @NotBlank(message = "Status is required.")
    private String status;

    @NotBlank(message = "Description is required.")
    private String description;
}
