package com.yourwatchrental.watchrental.watch.dto.request;

import com.yourwatchrental.watchrental.watch.enums.Status;
import jakarta.validation.constraints.NotNull;

public record WatchStatusUpdateRequestDTO(
        @NotNull(message = "Watch status is required")
        Status status
) {
}
