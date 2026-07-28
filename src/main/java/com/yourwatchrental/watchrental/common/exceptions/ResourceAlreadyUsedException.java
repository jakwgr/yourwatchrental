package com.yourwatchrental.watchrental.common.exceptions;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public abstract class ResourceAlreadyUsedException extends RuntimeException {
    public ResourceAlreadyUsedException(String message) {
        super(message);
        log.error(message + " " + LocalDateTime.now());
    }
}
