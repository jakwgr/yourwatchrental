package com.yourwatchrental.watchrental.branch.dto;

import com.yourwatchrental.watchrental.branch.BranchStatus;

import java.util.UUID;

public record BranchResponseDTO(
        UUID id,
        String city,
        String name,
        String address,
        String phoneNumber,
        String email,
        BranchStatus status
) {
};
