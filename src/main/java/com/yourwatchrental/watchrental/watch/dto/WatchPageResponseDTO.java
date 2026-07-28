package com.yourwatchrental.watchrental.watch.dto;

import com.yourwatchrental.watchrental.watch.enums.*;

import java.math.BigDecimal;
import java.util.UUID;

public record WatchPageResponseDTO(
        UUID id,
        String manufacturer,
        String model,
        String mainPhotoUrl,
        int yearOfProduction,
        BigDecimal pricePerDay,
        Condition condition,
        MovementType movementType,
        Status status,
        WatchType watchType
) {
}
