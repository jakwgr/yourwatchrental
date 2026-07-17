package com.yourwatchrental.watchrental.user.dto.response;

import com.yourwatchrental.watchrental.user.Role;
import com.yourwatchrental.watchrental.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO (
    UUID id,
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String email,
    String phoneNumber,
    LocalDateTime createdAt,
    Role role
) {
    public UserResponseDTO(User user)
    {
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getDateOfBirth(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getRole()
        );
    }
}