package com.yourwatchrental.watchrental.watch.watchhistory.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceNotFoundException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class WatchHistoryNotFoundException extends ResourceNotFoundException {
    public WatchHistoryNotFoundException(UUID id) {
        super(ErrorMessages.WATCH_HISTORY_NOT_FOUND.getMessage(), id);
    }
}
