package com.yourwatchrental.watchrental.branch.dto;

import com.yourwatchrental.watchrental.branch.BranchStatus;

public record BranchStatusUpdateRequestDTO(
        BranchStatus status
) {
}
