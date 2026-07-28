package com.yourwatchrental.watchrental.watch.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceNotFoundException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class WatchNotFoundException extends ResourceNotFoundException {
    public WatchNotFoundException(UUID id) {
        super(ErrorMessages.WATCH_NOT_FOUND.getMessage(), id);
    }
}
