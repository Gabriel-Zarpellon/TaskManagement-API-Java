package com.task_management.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @SuppressWarnings("unused")
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorMessageDTO> dto = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach(err -> {
            String message = err.getDefaultMessage();
            ErrorMessageDTO error = new ErrorMessageDTO();
            error.setError(message);
            dto.add(error);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(ExistingUserException.class)
    public ResponseEntity<?> handleExistingUserException(ExistingUserException e) {
        ErrorMessageDTO error = new ErrorMessageDTO();
        error.setError(e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    public GlobalExceptionHandler(MessageSource message) {
        this.messageSource = message;
    }
}