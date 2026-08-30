package com.yourwatchrental.watchrental.watch.dto.response;

import com.yourwatchrental.watchrental.branch.dto.BranchShortResponseDTO;
import com.yourwatchrental.watchrental.watch.enums.*;
import com.yourwatchrental.watchrental.watch.watchphoto.dto.WatchPhotoShortResponseDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record WatchFullInfoResponseDTO(
        UUID id,
        String manufacturer,
        String model,
        String referenceNumber,
        String serialNumber,
        String movement,
        String description,
        Integer yearOfProduction,
        BigDecimal pricePerDay,
        Condition condition,
        Gender gender,
        MovementType movementType,
        Status status,
        WatchType watchType,
        BranchShortResponseDTO branch,
        List<WatchPhotoShortResponseDTO> photos
) {
}
