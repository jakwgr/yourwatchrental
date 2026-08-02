package com.yourwatchrental.watchrental.rental.exception;

import com.yourwatchrental.watchrental.common.exceptions.ResourceWronglyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class PaymentStatusChangeException extends ResourceWronglyUsedException {
    public PaymentStatusChangeException(UUID id) {
        super(ErrorMessages.RENTAL_PAYMENT_STATUS_CHANGE_NOT_ALLOWED.getMessage(), id);
    }
}
