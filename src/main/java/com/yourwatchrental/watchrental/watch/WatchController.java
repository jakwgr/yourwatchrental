package com.yourwatchrental.watchrental.watch;

import com.yourwatchrental.watchrental.watch.dto.request.WatchFilterRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.request.WatchRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.request.WatchStatusUpdateRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchPageResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.request.WatchUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/watches")
@RequiredArgsConstructor
public class WatchController {
    private final WatchService watchService;

    @PostMapping
    public ResponseEntity<WatchResponseDTO> createWatch(@RequestBody @Valid WatchRequestDTO request) {
        WatchResponseDTO watch = watchService.createWatch(request);

        return ResponseEntity.ok(watch);
    }

    @GetMapping
    public ResponseEntity<Page<WatchPageResponseDTO>> getWatchPage(WatchFilterRequestDTO request, Pageable pageable) {
        Page<WatchPageResponseDTO> watches = watchService.getWatchPage(request, pageable);

        return ResponseEntity.ok(watches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WatchResponseDTO> getWatch(@PathVariable UUID id) {
        return ResponseEntity.ok(watchService.getWatch(id));
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<WatchResponseDTO> updateWatch(@PathVariable UUID id, @RequestBody @Valid WatchUpdateRequestDTO request)
    {
        return ResponseEntity.ok(watchService.updateWatch(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<WatchResponseDTO> updateWatchStatus(@PathVariable UUID id, @RequestBody @Valid WatchStatusUpdateRequestDTO request)
    {
        return ResponseEntity.ok(watchService.updateWatchStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> hardDeleteWatch(@PathVariable UUID id)
    {
        watchService.hardDeleteWatch(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
