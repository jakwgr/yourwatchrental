package com.yourwatchrental.watchrental.branch.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class BranchEmailUsedException extends ResourceAlreadyUsedException {
    public BranchEmailUsedException() {
        super(ErrorMessages.SAME_EMAIL.getMessage());
    }
}
