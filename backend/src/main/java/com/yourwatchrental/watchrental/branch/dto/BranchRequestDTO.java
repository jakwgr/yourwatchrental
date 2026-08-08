package com.yourwatchrental.watchrental.branch.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
        @Pattern(regexp = "^[0-9]{9}$", message = "Invalid phone number format. Expected format '123456789'")
        String phoneNumber,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {};
