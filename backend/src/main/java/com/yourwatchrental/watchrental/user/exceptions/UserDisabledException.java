package com.yourwatchrental.watchrental.user.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceNotFoundException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class UserDisabledException extends ResourceNotFoundException {
    public UserDisabledException(UUID id) {
        super(ErrorMessages.USER_DISABLED_LOGIN_REGISTER.getMessage(), id);
    }
}
