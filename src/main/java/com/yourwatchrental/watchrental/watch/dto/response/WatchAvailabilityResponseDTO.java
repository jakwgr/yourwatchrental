package com.yourwatchrental.watchrental.watch.dto.response;

import com.yourwatchrental.watchrental.rental.dto.response.RentalPeriodResponseDTO;

import java.util.List;
import java.util.UUID;

public record WatchAvailabilityResponseDTO(
        UUID id,
        List<RentalPeriodResponseDTO> unavailablePeriods
) {
}
