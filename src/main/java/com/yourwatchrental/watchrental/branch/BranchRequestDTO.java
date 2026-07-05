package com.yourwatchrental.watchrental.branch;

import java.util.UUID;

public record BranchRequestDTO(
        String city,
        String name,
        String address,
        String phoneNumber,
        String email
) {};
