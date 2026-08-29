package com.yourwatchrental.watchrental.email;

import com.yourwatchrental.watchrental.common.exceptions.ResourceNotFoundException;
import com.yourwatchrental.watchrental.common.exceptions.ResourceNotPossibleException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class EmailSendException extends ResourceNotPossibleException {
    public EmailSendException() {
        super(ErrorMessages.FAILED_EMAIL.getMessage());
    }
}
