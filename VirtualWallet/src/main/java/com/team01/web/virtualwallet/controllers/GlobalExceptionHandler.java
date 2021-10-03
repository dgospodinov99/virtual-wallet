package com.team01.web.virtualwallet.controllers;

import com.team01.web.virtualwallet.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.DateTimeException;
import java.time.LocalDateTime;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AuthenticationFailureException.class, UnauthorizedOperationException.class, BlockedUserException.class})
    private ResponseEntity<Object> unauthorized(RuntimeException e, HttpServletRequest request) {
        CustomJsonReturn json = new CustomJsonReturn(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name(),
                e.getMessage(),
                request.getServletPath());
        return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    private ResponseEntity<Object> entityNotFound(RuntimeException e, HttpServletRequest request) {
        CustomJsonReturn json = new CustomJsonReturn(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                e.getMessage(),
                request.getServletPath());

        return new ResponseEntity<>(json, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            DuplicateEntityException.class,
            InvalidTransferException.class})
    private ResponseEntity<Object> conflict(RuntimeException e, HttpServletRequest request) {
        CustomJsonReturn json = new CustomJsonReturn(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.name(),
                e.getMessage(),
                request.getServletPath());
        return new ResponseEntity<>(json, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {DateTimeException.class, InvalidUserInput.class, BadLuckException.class, InvalidPasswordException.class})
    private ResponseEntity<Object> badRequest(RuntimeException e, HttpServletRequest request) {
        CustomJsonReturn json = new CustomJsonReturn(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                e.getMessage(),
                request.getServletPath());
        return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
    }

    public void checkValidFields(BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidUserInput(result.getFieldError().getDefaultMessage());
        }
    }
}

class CustomJsonReturn {
    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

    private String path;

    public CustomJsonReturn(LocalDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
