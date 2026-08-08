package com.yourwatchrental.watchrental.rental.dto.request;

import com.yourwatchrental.watchrental.rental.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record RentalRequestDTO(

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        @NotNull(message = "Watch is required")
        UUID watchId

) {
}