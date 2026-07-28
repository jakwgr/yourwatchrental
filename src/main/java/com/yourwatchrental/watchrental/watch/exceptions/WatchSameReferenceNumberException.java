package com.yourwatchrental.watchrental.watch.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WatchSameReferenceNumberException extends ResourceAlreadyUsedException {
    public WatchSameReferenceNumberException() {
        super(ErrorMessages.WATCH_SAME_REFERENCE.getMessage());
        log.error(ErrorMessages.WATCH_SAME_REFERENCE.getMessage());
    }
}
