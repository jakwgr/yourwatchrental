package com.yourwatchrental.watchrental.user.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UserInformationUpdateRequestDTO(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,

        @Pattern(regexp = "^[0-9]{9}$", message = "Invalid phone number format. Expected format '123456789'")
        String phoneNumber
) {
}
