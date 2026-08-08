package com.yourwatchrental.watchrental.watch.dto.response;

import com.yourwatchrental.watchrental.watch.enums.Status;

import java.math.BigDecimal;
import java.util.UUID;

public record WatchCardResponseDTO(
        UUID id,
        String manufacturer,
        String model,
        BigDecimal pricePerDay,
        Status status,
        String branchName,
        String thumbnailUrl
) {
}
