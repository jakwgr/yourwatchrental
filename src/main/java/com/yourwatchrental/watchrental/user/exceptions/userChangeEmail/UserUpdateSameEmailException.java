package com.yourwatchrental.watchrental.user.exceptions.userChangeEmail;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class UserUpdateSameEmailException extends ResourceAlreadyUsedException {
    public UserUpdateSameEmailException() {
        super(ErrorMessages.USER_UPDATE_SAME_EMAIL.getMessage());
    }
}
