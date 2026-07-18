package com.yourwatchrental.watchrental.user.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceNotFoundException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserIdNotFoundExcpetion extends ResourceNotFoundException {
    public UserIdNotFoundExcpetion() {
        super(ErrorMessages.ID_NOT_FOUND.getMessage() + ", try to search by other specifications", null);
        log.error(ErrorMessages.ID_NOT_FOUND.getMessage());
    }
}
