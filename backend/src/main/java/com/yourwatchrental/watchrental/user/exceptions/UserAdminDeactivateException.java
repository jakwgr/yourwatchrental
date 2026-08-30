package com.yourwatchrental.watchrental.user.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceForbiddenException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class UserAdminDeactivateException extends ResourceForbiddenException {
    public UserAdminDeactivateException(UUID id) {
        super(ErrorMessages.ADMIN_SELF_DELETE.getMessage(), id);
    }
}
