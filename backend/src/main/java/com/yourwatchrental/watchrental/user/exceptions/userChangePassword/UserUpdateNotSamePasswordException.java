package com.yourwatchrental.watchrental.user.exceptions.userChangePassword;

import com.yourwatchrental.watchrental.common.exceptions.ResourceNotFoundException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class UserUpdateNotSamePasswordException extends ResourceNotFoundException {
    public UserUpdateNotSamePasswordException(UUID id) {
        super(ErrorMessages.USER_UPDATE_NOT_SAME_NEW_PASSWORDS.getMessage(), id);
    }
}
