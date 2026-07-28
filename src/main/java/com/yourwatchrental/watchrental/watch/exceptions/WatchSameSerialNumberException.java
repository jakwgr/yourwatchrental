package com.yourwatchrental.watchrental.watch.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WatchSameSerialNumberException extends ResourceAlreadyUsedException {
    public WatchSameSerialNumberException() {
        super(ErrorMessages.WATCH_SAME_SERIAL.getMessage());
        log.error(ErrorMessages.WATCH_SAME_SERIAL.getMessage());
    }
}
