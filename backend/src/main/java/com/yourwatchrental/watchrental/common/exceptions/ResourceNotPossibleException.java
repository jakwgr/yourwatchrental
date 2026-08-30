package com.yourwatchrental.watchrental.common.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ResourceNotPossibleException extends RuntimeException{
    protected ResourceNotPossibleException(String message) {
        super(message);
        log.error(message);
    }
}