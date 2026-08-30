package com.yourwatchrental.watchrental.rental.exception;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class RentalConflictException extends ResourceAlreadyUsedException {
    public RentalConflictException() {
        super(ErrorMessages.RENTAL_EXISTS.getMessage());
    }
}
