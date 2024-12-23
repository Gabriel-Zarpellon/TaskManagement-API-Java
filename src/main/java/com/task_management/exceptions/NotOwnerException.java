package com.task_management.exceptions;

public class NotOwnerException extends Exception {
    public NotOwnerException() {
        super("User isn't task owner.");
    }

    public NotOwnerException(String message) {
        super(message);
    }
}
