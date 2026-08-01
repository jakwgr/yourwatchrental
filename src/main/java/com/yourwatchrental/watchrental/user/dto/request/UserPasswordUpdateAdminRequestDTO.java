package com.yourwatchrental.watchrental.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordUpdateAdminRequestDTO(
        @NotBlank(message = "Password is required")
        @Size(min = 5, message = "Password must contains 5 or more characters")
        String newPassword1,

        @NotBlank(message = "Password is required")
        @Size(min = 5, message = "Password must contains 5 or more characters")
        String newPassword
) {
}
