package com.yourwatchrental.watchrental.watch.watchphoto;

import com.yourwatchrental.watchrental.watch.watchphoto.dto.WatchPhotoRequestDTO;
import com.yourwatchrental.watchrental.watch.watchphoto.dto.WatchPhotoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/watches/photos")
@RequiredArgsConstructor
public class WatchPhotoController {
    private final WatchPhotoService watchPhotoService;

    @PostMapping("/{watchId}")
    public ResponseEntity<WatchPhotoResponseDTO> createPhoto(
            @PathVariable UUID watchId,
            @RequestBody @Valid WatchPhotoRequestDTO request
    ) {
        return ResponseEntity.ok(
                watchPhotoService.createPhoto(watchId, request)
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
