package com.yourwatchrental.watchrental.user.dto.request;

import com.yourwatchrental.watchrental.user.UserStatus;
import jakarta.validation.constraints.NotNull;

public record UserStatusChangeRequestDTO(
        @NotNull
        UserStatus status
) {
}
