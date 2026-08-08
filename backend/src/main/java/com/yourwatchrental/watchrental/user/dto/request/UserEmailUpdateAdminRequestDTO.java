package com.yourwatchrental.watchrental.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserEmailUpdateAdminRequestDTO(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {
}
