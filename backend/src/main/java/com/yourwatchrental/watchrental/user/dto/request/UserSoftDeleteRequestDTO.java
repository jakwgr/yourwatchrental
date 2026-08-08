package com.yourwatchrental.watchrental.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserSoftDeleteRequestDTO(
        @NotBlank(message = "Password is required")
        @Size(min = 5, message = "Password must contains 5 or more characters")
        String password
) {
}
