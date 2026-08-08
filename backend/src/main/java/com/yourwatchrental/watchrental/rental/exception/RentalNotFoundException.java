package com.yourwatchrental.watchrental.rental.exception;

import com.yourwatchrental.watchrental.common.exceptions.ResourceNotFoundException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class RentalNotFoundException extends ResourceNotFoundException {
    public RentalNotFoundException(UUID id) {
        super(ErrorMessages.RENTAL_NOT_FOUND.getMessage(), id);
    }
}
