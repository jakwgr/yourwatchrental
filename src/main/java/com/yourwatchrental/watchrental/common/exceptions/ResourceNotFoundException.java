package com.yourwatchrental.watchrental.common.exceptions;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public abstract class ResourceNotFoundException extends RuntimeException{
    protected ResourceNotFoundException(String message, UUID id) {
        super(message);
        log.error(message + " id: " + id);
    }
}
