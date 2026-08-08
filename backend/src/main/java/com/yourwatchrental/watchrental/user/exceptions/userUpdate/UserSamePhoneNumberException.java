package com.yourwatchrental.watchrental.user.exceptions.userUpdate;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class UserSamePhoneNumberException extends ResourceAlreadyUsedException {
    public UserSamePhoneNumberException() {
        super(ErrorMessages.USER_UPDATE_SAME_PHONE_NUMBER.getMessage());
    }
}
