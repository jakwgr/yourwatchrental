package com.yourwatchrental.watchrental.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserPasswordUpdateRequestDTO(
        @NotBlank(message = "Password is required")
        String newPassword,

        @NotBlank(message = "Password is required")
        String newPassword1,

        @NotBlank(message = "Password is required")
        String password
) {
}
