package com.yourwatchrental.watchrental.branch.dto;

public record BranchFilterCriteriaRequest(
        String city,
        String name,
        String phoneNumber,
        String address,
        String email
) {
}
