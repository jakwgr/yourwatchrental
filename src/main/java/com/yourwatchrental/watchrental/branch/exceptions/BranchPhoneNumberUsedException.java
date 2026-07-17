package com.yourwatchrental.watchrental.branch.exceptions;

import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;
import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;

public class BranchPhoneNumberUsedException extends ResourceAlreadyUsedException {
    public BranchPhoneNumberUsedException() {
        super(ErrorMessages.SAME_PHONE_NUMBER.getMessage());
    }
}
