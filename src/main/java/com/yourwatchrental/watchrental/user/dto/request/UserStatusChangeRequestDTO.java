package com.yourwatchrental.watchrental.user.dto.request;

import com.yourwatchrental.watchrental.user.UserStatus;
import com.yourwatchrental.watchrental.watch.enums.Status;
import jakarta.validation.constraints.NotNull;

public record UserStatusChangeRequestDTO(
        @NotNull
        UserStatus status
) {
}
