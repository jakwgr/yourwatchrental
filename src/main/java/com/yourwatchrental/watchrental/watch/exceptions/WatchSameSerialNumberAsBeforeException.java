package com.yourwatchrental.watchrental.watch.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class WatchSameSerialNumberAsBeforeException extends ResourceAlreadyUsedException {
    public WatchSameSerialNumberAsBeforeException() {
        super(ErrorMessages.WATCH_SAME_SERIAL_AS_BEFORE.getMessage());
    }
}
