package com.yourwatchrental.watchrental.security;

import com.yourwatchrental.watchrental.common.exceptions.ResourceNotPossibleException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class InvalidPasswordResetTokenException extends ResourceNotPossibleException {
    public InvalidPasswordResetTokenException() {
        super(ErrorMessages.INVALID_RESET_TOKEN.getMessage());
    }
}
