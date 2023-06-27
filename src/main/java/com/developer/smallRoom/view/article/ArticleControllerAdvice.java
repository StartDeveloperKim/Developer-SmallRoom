package com.developer.smallRoom.view.article;

import com.developer.smallRoom.global.exception.auth.NotAuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(value = "com.developer.smallRoom.view.article")
public class ArticleControllerAdvice {

    @ExceptionHandler({NotAuthorizationException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult()
                .getAllErrors()
                .forEach(objectError -> errors.put(((FieldError) objectError).getField(), objectError.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
