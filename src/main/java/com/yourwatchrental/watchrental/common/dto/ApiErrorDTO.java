package com.yourwatchrental.watchrental.common.dto;

import java.time.LocalDateTime;


public record ApiErrorDTO(
        String message,
        LocalDateTime timestamp
) {
}
