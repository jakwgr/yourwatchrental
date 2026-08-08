package com.yourwatchrental.watchrental.branch.dto;

import java.util.UUID;

public record BranchShortResponseDTO(
        UUID id,
        String city,
        String name,
        String address,
        String phoneNumber
) {
}
