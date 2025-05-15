package com.vasilev.news.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@ControllerAdvice
public class FeedExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {

        List<ErrorResponse> validationErrors = new ArrayList<>();

        ex.getConstraintViolations()
                .forEach(v -> validationErrors.add(
                        new ErrorResponse(v.getMessage())
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("errors", validationErrors);

        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }
}

record ErrorResponse(String error) {}
