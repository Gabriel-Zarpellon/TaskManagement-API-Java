package com.task_management.exceptions;

public class ExistingUserException extends Exception {
    public ExistingUserException() {
        super("user already registered.");
    }

    public ExistingUserException(String message) {
        super(message);
    }
}
