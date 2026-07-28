package com.yourwatchrental.watchrental.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserSoftDeleteRequestDTO(
        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Confirmation is necessary")
        String deleteConfirm
) {
}
