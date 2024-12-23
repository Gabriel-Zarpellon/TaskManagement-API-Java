package com.task_management.exceptions;

public class NotFoundException extends Exception {
    public NotFoundException() {
        super("Task not found.");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
