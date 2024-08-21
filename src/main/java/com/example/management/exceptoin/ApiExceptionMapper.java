package com.example.management.exceptoin;


import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Component
public class ApiExceptionMapper {
    @ExceptionHandler({ ApiException.class })
    public ResponseEntity<Object> handleAll(ApiException ex, WebRequest request) {
        return new ResponseEntity<Object>(
                ex, new HttpHeaders(), ex.getStatusCode());
    }
}
