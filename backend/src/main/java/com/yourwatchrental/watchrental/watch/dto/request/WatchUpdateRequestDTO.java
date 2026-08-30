package com.yourwatchrental.watchrental.watch.dto.request;

import com.yourwatchrental.watchrental.watch.enums.Condition;
import com.yourwatchrental.watchrental.watch.enums.Gender;
import com.yourwatchrental.watchrental.watch.enums.MovementType;
import com.yourwatchrental.watchrental.watch.enums.WatchType;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record WatchUpdateRequestDTO(
        String manufacturer,

        String model,

        String referenceNumber,

        String movement,

        String description,

        @PositiveOrZero(message = "Year of production needs to be accurate")
        int yearOfProduction,

        @PositiveOrZero(message = "Price per day needs to be accurate")
        BigDecimal pricePerDay,

        Condition condition,

        Gender gender,

        MovementType movementType,

        WatchType watchType
) {
}
