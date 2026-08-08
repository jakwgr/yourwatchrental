package com.yourwatchrental.watchrental.user.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class UserWrongLoginException extends ResourceAlreadyUsedException {
    public UserWrongLoginException() {
        super(ErrorMessages.USER_CANNOT_LOGIN.getMessage());
    }
}
