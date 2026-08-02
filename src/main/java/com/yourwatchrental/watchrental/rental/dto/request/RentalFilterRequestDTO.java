package com.yourwatchrental.watchrental.rental.dto.request;

import com.yourwatchrental.watchrental.rental.PaymentMethod;
import com.yourwatchrental.watchrental.rental.PaymentStatus;
import com.yourwatchrental.watchrental.rental.RentalStatus;

import java.time.LocalDate;
import java.util.UUID;

public record RentalFilterRequestDTO(
        RentalStatus rentalStatus,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod,
        UUID userId,
        UUID watchId,
        UUID branchId,
        LocalDate startDateFrom,
        LocalDate startDateTo,
        LocalDate endDateFrom,
        LocalDate endDateTo
) {}