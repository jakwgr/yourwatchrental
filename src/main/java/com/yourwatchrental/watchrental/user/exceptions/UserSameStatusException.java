package com.yourwatchrental.watchrental.user.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class UserSameStatusException extends ResourceAlreadyUsedException {
    public UserSameStatusException() {
        super(ErrorMessages.USER_UPDATE_SAME_STATUS.getMessage());
    }
}
