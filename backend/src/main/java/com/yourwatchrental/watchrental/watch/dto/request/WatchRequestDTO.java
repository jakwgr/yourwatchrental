package com.yourwatchrental.watchrental.watch.dto.request;

import com.yourwatchrental.watchrental.watch.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record WatchRequestDTO(
        @NotBlank(message = "Manufacturer is required")
        String manufacturer,

        @NotBlank(message = "Model is required")
        String model,

        @NotBlank(message = "Reference number is required")
        String referenceNumber,

        @NotBlank(message = "Serial number is required")
        String serialNumber,

        @NotBlank(message = "Movement is required")
        String movement,

        String description,

        @NotNull(message = "Year of production is required")
        @PositiveOrZero(message = "Year of production needs to be accurate")
        int yearOfProduction,

        @NotNull(message = "Price per day is required")
        @PositiveOrZero(message = "Price per day needs to be accurate")
        BigDecimal pricePerDay,

        @NotNull(message = "Condition is required")
        Condition condition,

        @NotNull(message = "Gender is required")
        Gender gender,

        @NotNull(message = "Movement is required")
        MovementType movementType,

        @NotNull(message = "Watch status is required")
        Status status,

        @NotNull(message = "Watch type is required")
        WatchType watchType,

        @NotNull(message = "Branch is required")
        UUID branchId
) {
}
