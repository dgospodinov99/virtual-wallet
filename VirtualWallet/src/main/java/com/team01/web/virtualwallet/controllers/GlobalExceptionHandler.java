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

    @ExceptionHandler(value = {AuthenticationFailureException.class, UnauthorizedOperationException.class})
    private ResponseEntity<JsonResponse> unauthorized(RuntimeException e, HttpServletRequest request) {
        return new ResponseEntity<JsonResponse>(getJson(e,request, HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(value = EntityNotFoundException.class)
    private ResponseEntity<JsonResponse> entityNotFound(RuntimeException e, HttpServletRequest request) {
        return new ResponseEntity<JsonResponse>(getJson(e,request, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            DuplicateEntityException.class,
            InvalidTransferException.class})
    private ResponseEntity<JsonResponse> conflict(RuntimeException e, HttpServletRequest request) {
        return new ResponseEntity<JsonResponse>(getJson(e,request,HttpStatus.CONFLICT), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {DateTimeException.class, InvalidUserInput.class, BadLuckException.class, InvalidPasswordException.class, BlockedUserException.class})
    private ResponseEntity<JsonResponse> badRequest(RuntimeException e, HttpServletRequest request) {
        return new ResponseEntity<JsonResponse>(getJson(e,request,HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    public void checkValidFields(BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidUserInput(result.getFieldError().getDefaultMessage());
        }
    }

    private JsonResponse getJson(RuntimeException e, HttpServletRequest request, HttpStatus status){
        return new JsonResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                e.getMessage(),
                request.getServletPath());
    }

}

class JsonResponse {
    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

    private String path;

    public JsonResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
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
