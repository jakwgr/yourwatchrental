package com.yourwatchrental.watchrental.user.exceptions.userChangePassword;

import com.yourwatchrental.watchrental.common.exceptions.ResourceNotFoundException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class UserUpdatePasswordChangeDoesNotMatchException extends ResourceNotFoundException {
    public UserUpdatePasswordChangeDoesNotMatchException(UUID id) {
        super(ErrorMessages.USER_UPDATE_PASSWORD_DOESNT_MATCH.getMessage(), id);
    }
}
