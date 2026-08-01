package com.yourwatchrental.watchrental.common.handlers;

import com.yourwatchrental.watchrental.common.dto.ApiErrorDTO;
import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.exceptions.ResourceForbiddenException;
import com.yourwatchrental.watchrental.common.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExcpetionHandler {

    @ExceptionHandler(ResourceForbiddenException.class)
    public ResponseEntity<ApiErrorDTO> ResourceForbiddenException(ResourceForbiddenException ex)
    {
        ApiErrorDTO error = new ApiErrorDTO(
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> ResourceNotFound(ResourceNotFoundException ex)
    {
        ApiErrorDTO error = new ApiErrorDTO(
               ex.getMessage(),
                LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(ResourceAlreadyUsedException.class)
    public ResponseEntity<ApiErrorDTO> ResourceAlreadyUsed(ResourceAlreadyUsedException ex)
    {
        ApiErrorDTO error = new ApiErrorDTO(
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidationException(MethodArgumentNotValidException ex)
    {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ApiErrorDTO error = new ApiErrorDTO(
                errorMessage,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}
