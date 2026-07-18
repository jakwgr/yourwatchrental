package com.yourwatchrental.watchrental.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record UserFilterCriteriaRequestDTO(
        String id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {

}
