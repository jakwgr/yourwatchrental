package com.yourwatchrental.watchrental.rental.dto.response;

import java.time.LocalDate;

public record RentalPeriodResponseDTO(
        LocalDate startDate,
        LocalDate endDate
) {
}
