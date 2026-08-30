package com.yourwatchrental.watchrental.user.dto.request;

public record UserFilterCriteriaRequestDTO(
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {

}
