package com.yourwatchrental.watchrental.rental.dto.request;

import com.yourwatchrental.watchrental.rental.PaymentStatus;

public record PaymentStatusChangeRequestDTO(
        PaymentStatus paymentStatus
) {
}
