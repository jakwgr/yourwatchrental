package com.yourwatchrental.watchrental.watch.watchphoto.dto;

import com.yourwatchrental.watchrental.watch.watchphoto.PhotoType;

import java.util.UUID;

public record WatchPhotoResponseDTO(
        UUID id,
        String photoUrl,
        PhotoType photoType,
        String description,
        UUID watchId
) {
}
