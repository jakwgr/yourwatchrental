package com.yourwatchrental.watchrental.rental.dto.request;

import com.yourwatchrental.watchrental.rental.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public record PaymentStatusChangeRequestDTO(
        @NotNull(message = "Payment status is required")
        PaymentStatus paymentStatus
) {
}
