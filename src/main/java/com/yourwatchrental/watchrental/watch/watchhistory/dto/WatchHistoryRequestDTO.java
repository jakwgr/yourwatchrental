package com.yourwatchrental.watchrental.watch.watchhistory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record WatchHistoryRequestDTO(
        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Date is required")
        LocalDate date
) {
}
