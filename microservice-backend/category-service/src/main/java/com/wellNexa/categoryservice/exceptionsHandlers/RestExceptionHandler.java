package com.wellNexa.categoryservice.exceptionsHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wellNexa.categoryservice.dtos.ApiResponseDto;
import com.wellNexa.categoryservice.exceptions.CategoryAlreadyExistsException;
import com.wellNexa.categoryservice.exceptions.CategoryNotFoundException;
import com.wellNexa.categoryservice.exceptions.ServiceLogicException;


@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = ServiceLogicException.class)
    public ResponseEntity<ApiResponseDto<?>> ServiceLogicExceptionHandler(ServiceLogicException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponseDto.builder()
                        .isSuccess(false)
                        .message(exception.getMessage())
                        .build()
        );
    }
    @ExceptionHandler(value = CategoryNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> CategoryNotFoundExceptionHandler(CategoryNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponseDto.builder()
                        .isSuccess(false)
                        .message(exception.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = CategoryAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<?>> CategoryAlreadyExistsExceptionHandler(CategoryAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponseDto.builder()
                        .isSuccess(false)
                        .message(exception.getMessage())
                        .build()
        );
    }

}
