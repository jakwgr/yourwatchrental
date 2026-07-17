package com.yourwatchrental.watchrental.branch.exceptions;

import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;
import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;

public class BranchEmailUsedException extends ResourceAlreadyUsedException {
    public BranchEmailUsedException() {
        super(ErrorMessages.SAME_EMAIL.getMessage());
    }
}
