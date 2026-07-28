package com.yourwatchrental.watchrental.watch.dto.response;

import com.yourwatchrental.watchrental.watch.enums.*;

import java.math.BigDecimal;
import java.util.UUID;

public record WatchResponseDTO(
        UUID id,
        String manufacturer,
        String model,
        String referenceNumber,
        String serialNumber,
        String movement,
        String description,
        int yearOfProduction,
        BigDecimal pricePerDay,
        Condition condition,
        Gender gender,
        MovementType movementType,
        Status status,
        WatchType watchType
) {
}
