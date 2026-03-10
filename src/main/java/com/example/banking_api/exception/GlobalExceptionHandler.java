package com.example.banking_api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<?> handleOptimisticLock() {
        Map<String,Object> error = new HashMap<>();
        error.put("message","Concurrent update detected. Please retry.");
        error.put("status",409);
        error.put("time",LocalDateTime.now());
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, Object> error = new HashMap<>();
        error.put("message",
                ex.getBindingResult().getFieldError().getDefaultMessage());
        error.put("status",400);
        error.put("time",LocalDateTime.now());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex){

        Map<String,Object> error = new HashMap<>();
        error.put("message","Method not allowed");
        error.put("status",405);
        error.put("time",LocalDateTime.now());

        return ResponseEntity.status(405).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex){

        Map<String,Object> error = new HashMap<>();
        error.put("message",ex.getMessage());
        error.put("status",400);
        error.put("time",LocalDateTime.now());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex){

        Map<String,Object> error = new HashMap<>();
        error.put("message","Internal server error");
        error.put("status",500);
        error.put("time",LocalDateTime.now());

        return ResponseEntity.status(500).body(error);
    }
}