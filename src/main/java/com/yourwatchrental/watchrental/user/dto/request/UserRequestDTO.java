package com.yourwatchrental.watchrental.user.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserRequestDTO(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Invalid birth date")
        LocalDate dateOfBirth,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[0-9]{9}$", message = "Invalid phone number format. Expected format '123456789'")
        String phoneNumber,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        String password

)
{ }
