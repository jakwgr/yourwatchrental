package com.yourwatchrental.watchrental.user.exceptions.userUpdate;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class UserSameRoleException extends ResourceAlreadyUsedException {
    public UserSameRoleException() {
        super(ErrorMessages.USER_UPDATE_SAME_ROLE.getMessage());
    }
}