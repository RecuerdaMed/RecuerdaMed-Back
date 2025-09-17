package com.hackaton.recuerdamed.shared.exception;

import com.hackaton.recuerdamed.shared.exception.custom_exception.DrugNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DrugNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDrugNotFoundException(DrugNotFoundException exception, WebRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .path(request.getDescription(false).substring(4))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}