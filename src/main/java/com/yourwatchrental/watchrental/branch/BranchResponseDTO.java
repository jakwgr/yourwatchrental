package com.yourwatchrental.watchrental.branch;

import java.util.UUID;

public record BranchResponseDTO(
        UUID id,
        String city,
        String name,
        String address,
        String phoneNumber,
        String email
) {
    public BranchResponseDTO(Branch branch)
    {
       this(
               branch.getId(),
               branch.getCity(),
               branch.getName(),
               branch.getAddress(),
               branch.getPhoneNumber(),
               branch.getEmail()
       );
    }
};
