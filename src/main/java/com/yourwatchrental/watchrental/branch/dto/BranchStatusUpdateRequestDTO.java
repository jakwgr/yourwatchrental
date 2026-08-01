package com.yourwatchrental.watchrental.branch.dto;

import com.yourwatchrental.watchrental.branch.BranchStatus;
import jakarta.validation.constraints.NotNull;

public record BranchStatusUpdateRequestDTO(
        @NotNull(message = "Status is required")
        BranchStatus status
) {
}
