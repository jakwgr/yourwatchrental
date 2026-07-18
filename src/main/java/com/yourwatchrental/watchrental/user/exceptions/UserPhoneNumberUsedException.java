package com.yourwatchrental.watchrental.user.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class UserPhoneNumberUsedException extends ResourceAlreadyUsedException {
    public UserPhoneNumberUsedException() {
        super(ErrorMessages.SAME_PHONE_NUMBER.getMessage());
    }
}
