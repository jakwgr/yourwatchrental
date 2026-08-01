package com.yourwatchrental.watchrental.branch.dto;

import com.yourwatchrental.watchrental.branch.BranchStatus;

public record BranchFilterCriteriaRequest(
        String city,
        String name,
        String phoneNumber,
        String address,
        String email,
        BranchStatus status
) {
}
