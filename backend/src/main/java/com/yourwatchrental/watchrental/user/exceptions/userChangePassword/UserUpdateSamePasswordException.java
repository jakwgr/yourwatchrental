package com.yourwatchrental.watchrental.user.exceptions.userChangePassword;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class UserUpdateSamePasswordException extends ResourceAlreadyUsedException {
    public UserUpdateSamePasswordException() {
        super(ErrorMessages.USER_UPDATE_SAME_PASSWORD.getMessage());
    }
}
