package com.yourwatchrental.watchrental.watch.dto.request;

import com.yourwatchrental.watchrental.watch.enums.*;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record WatchFilterRequestDTO(
        UUID watchId,
        String manufacturer,
        String model,
        String referenceNumber,
        String serialNumber,
        String movement,
        Condition condition,
        Gender gender,
        MovementType movementType,
        Status status,
        WatchType watchType,
        UUID branchId,

        @PositiveOrZero(message = "Minimum price needs to be accurate")
        BigDecimal minPrice,
        @PositiveOrZero(message = "Maximum price needs to be accurate")
        BigDecimal maxPrice,


        @PositiveOrZero(message = "Minimum year needs to be accurate")
        Integer minYear,
        @PositiveOrZero(message = "Maximum year needs to be accurate")
        Integer maxYear
) {
}
