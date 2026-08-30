package com.yourwatchrental.watchrental.watch.dto.response;

import com.yourwatchrental.watchrental.watch.enums.Condition;
import com.yourwatchrental.watchrental.watch.enums.Status;
import com.yourwatchrental.watchrental.watch.enums.WatchType;

import java.math.BigDecimal;
import java.util.UUID;

public record WatchPageResponseDTO(
        UUID id,
        String manufacturer,
        String model,
        String referenceNumber,
        String serialNumber,
        String mainPhotoUrl,
        int yearOfProduction,
        BigDecimal pricePerDay,
        Condition condition,
        Status status,
        WatchType watchType
) {
}
