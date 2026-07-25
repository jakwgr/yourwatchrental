package com.yourwatchrental.watchrental.user.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceNotFoundException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class UserWrongPasswordException extends ResourceNotFoundException {
    public UserWrongPasswordException(UUID id) {
        super(ErrorMessages.USER_UPDATE_WRONG_PASSWORD.getMessage(), id);
    }
}
