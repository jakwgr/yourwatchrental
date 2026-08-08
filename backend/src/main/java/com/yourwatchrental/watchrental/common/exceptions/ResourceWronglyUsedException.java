package com.yourwatchrental.watchrental.common.exceptions;

import com.yourwatchrental.watchrental.common.handlers.GlobalExcpetionHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class ResourceWronglyUsedException extends RuntimeException {
    public ResourceWronglyUsedException(String message, UUID id) {
        super(message);
        log.error(message + " id: " + id);
    }
}
