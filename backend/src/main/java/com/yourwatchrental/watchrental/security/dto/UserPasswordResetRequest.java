package com.yourwatchrental.watchrental.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserPasswordResetRequest(
        @NotNull
        UUID userId,

        @NotBlank
        String token,

        @NotBlank
        @Size(min = 5)
        String newPassword
) {
}
