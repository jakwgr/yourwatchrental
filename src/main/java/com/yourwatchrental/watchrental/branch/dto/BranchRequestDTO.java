package com.yourwatchrental.watchrental.branch.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.web.bind.annotation.ExceptionHandler;

public record BranchRequestDTO(
        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$",
                message = "Invalid phone number format. Expected format: +48 123 456 789 or 123456789"
        )
        String phoneNumber,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {};
