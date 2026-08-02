package com.yourwatchrental.watchrental.rental.exception;

import com.yourwatchrental.watchrental.common.exceptions.ResourceForbiddenException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class RentalForbiddenExcpetion extends ResourceForbiddenException {
    public RentalForbiddenExcpetion(UUID id) {
        super(ErrorMessages.RESOURCE_FORBIDDEN.getMessage(), id);
    }
}
