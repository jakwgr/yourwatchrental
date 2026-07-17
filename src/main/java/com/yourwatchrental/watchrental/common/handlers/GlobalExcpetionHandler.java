package com.yourwatchrental.watchrental.common.handlers;

import com.yourwatchrental.watchrental.common.dto.ApiErrorDTO;
import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExcpetionHandler {

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
}
