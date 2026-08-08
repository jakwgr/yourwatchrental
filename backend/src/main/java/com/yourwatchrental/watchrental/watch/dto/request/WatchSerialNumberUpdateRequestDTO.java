package com.yourwatchrental.watchrental.watch.dto.request;

import java.util.UUID;

public record WatchSerialNumberUpdateRequestDTO(
        UUID id,
        String serialNumber
) {
}
