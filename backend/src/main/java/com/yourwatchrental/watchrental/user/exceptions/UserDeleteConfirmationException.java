package com.yourwatchrental.watchrental.user.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceWronglyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class UserDeleteConfirmationException extends ResourceWronglyUsedException {
    public UserDeleteConfirmationException( UUID id) {
        super(ErrorMessages.USER_DELETE_CONFIRM.getMessage(), id);
    }
}
