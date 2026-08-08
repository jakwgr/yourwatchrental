package com.yourwatchrental.watchrental.rental.exception;

import com.yourwatchrental.watchrental.common.exceptions.ResourceWronglyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class RentalBadDateRangeException extends ResourceWronglyUsedException {
    public RentalBadDateRangeException(UUID id) {
        super(ErrorMessages.RENTAL_BAD_DATE_RANGE.getMessage(), id);
    }
}
