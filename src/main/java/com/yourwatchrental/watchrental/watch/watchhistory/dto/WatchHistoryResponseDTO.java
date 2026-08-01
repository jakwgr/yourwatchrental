package com.yourwatchrental.watchrental.watch.watchhistory.dto;

import java.time.LocalDate;
import java.util.UUID;

public record WatchHistoryResponseDTO(
        UUID id,
        String description,
        LocalDate date,
        UUID watchId
) {
}
