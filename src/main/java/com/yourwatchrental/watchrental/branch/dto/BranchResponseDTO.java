package com.yourwatchrental.watchrental.branch.dto;

import java.util.UUID;

public record BranchResponseDTO(
        UUID id,
        String city,
        String name,
        String address,
        String phoneNumber,
        String email
) {};
