package com.yourwatchrental.watchrental.watch.watchphoto;

import com.yourwatchrental.watchrental.watch.watchphoto.dto.WatchPhotoRequestDTO;
import com.yourwatchrental.watchrental.watch.watchphoto.dto.WatchPhotoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/watches/photos")
@RequiredArgsConstructor
public class WatchPhotoController {
    private final WatchPhotoService watchPhotoService;

    @PostMapping(value = "/{watchId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WatchPhotoResponseDTO> createPhoto(
            @PathVariable UUID watchId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("photoType") String photoType,
            @RequestPart("description") String description
    ) {
        WatchPhotoRequestDTO request =
                new WatchPhotoRequestDTO(
                        PhotoType.valueOf(photoType),
                        description
                );

        return ResponseEntity.ok(
                watchPhotoService.createPhoto(watchId, file, request)
        );
    }
    @GetMapping("/{watchId}")
    public ResponseEntity<List<WatchPhotoResponseDTO>> getPhotosByWatch(
            @PathVariable UUID watchId)
    {
        return ResponseEntity.ok(
                watchPhotoService.getPhotosByWatch(watchId)
        );
    }

    @GetMapping("/{watchId}/thumbnail")
    public ResponseEntity<WatchPhotoResponseDTO> getThumbnail(
            @PathVariable UUID watchId)
    {
        return ResponseEntity.ok(
                watchPhotoService.getThumbnail(watchId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable UUID id)
    {
        watchPhotoService.deletePhoto(id);

        return ResponseEntity.noContent().build();
    }
}
