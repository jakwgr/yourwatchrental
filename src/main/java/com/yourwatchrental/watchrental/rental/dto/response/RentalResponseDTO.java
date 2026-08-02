package com.yourwatchrental.watchrental.rental.dto.response;

import com.yourwatchrental.watchrental.rental.PaymentMethod;
import com.yourwatchrental.watchrental.rental.PaymentStatus;
import com.yourwatchrental.watchrental.rental.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RentalResponseDTO(

        UUID id,

        LocalDate startDate,

        LocalDate endDate,

        BigDecimal totalPrice,

        PaymentMethod paymentMethod,

        RentalStatus rentalStatus,

        PaymentStatus paymentStatus,

        UUID branchId,

        UUID userId,

        UUID watchId

) {
}