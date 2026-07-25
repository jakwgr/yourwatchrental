package com.yourwatchrental.watchrental.user.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class UserEmailUsedException extends ResourceAlreadyUsedException {
    public UserEmailUsedException() {
        super(ErrorMessages.SAME_EMAIL.getMessage());
    }
}
