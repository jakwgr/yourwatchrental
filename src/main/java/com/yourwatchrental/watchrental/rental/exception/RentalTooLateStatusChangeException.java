package com.yourwatchrental.watchrental.rental.exception;

import com.yourwatchrental.watchrental.common.exceptions.ResourceWronglyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class RentalTooLateStatusChangeException extends ResourceWronglyUsedException {
    public RentalTooLateStatusChangeException(UUID id) {
        super(ErrorMessages.RENTAL_TOO_LATE_CHANGE_CANCEL.getMessage(), id);
    }
}
