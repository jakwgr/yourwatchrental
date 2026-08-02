package com.yourwatchrental.watchrental.rental.exception;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class RentalWatchNotAvailableException extends ResourceAlreadyUsedException {
    public RentalWatchNotAvailableException()
    {
        super(ErrorMessages.RENTAL_WATCH_NOT_AVAILABLE.getMessage());
    }
}
