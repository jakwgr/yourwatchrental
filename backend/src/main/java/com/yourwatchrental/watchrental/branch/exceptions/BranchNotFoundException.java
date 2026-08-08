package com.yourwatchrental.watchrental.branch.exceptions;

import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;
import com.yourwatchrental.watchrental.common.exceptions.ResourceNotFoundException;

import java.util.UUID;

public class BranchNotFoundException extends ResourceNotFoundException {
    public BranchNotFoundException(UUID id) {
        super(ErrorMessages.BRANCH_NOT_FOUND.getMessage(), id);
    }
}
