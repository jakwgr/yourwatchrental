package com.yourwatchrental.watchrental.watch.watchphoto.dto;

import com.yourwatchrental.watchrental.watch.watchphoto.PhotoType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WatchPhotoRequestDTO(
        @NotBlank
        String photoUrl,

        @NotNull
        PhotoType photoType,

        @NotBlank
        String description
) {
}
