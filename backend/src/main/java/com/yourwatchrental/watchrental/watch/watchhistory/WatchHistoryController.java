package com.yourwatchrental.watchrental.watch.watchhistory;

import com.yourwatchrental.watchrental.watch.watchhistory.dto.WatchHistoryRequestDTO;
import com.yourwatchrental.watchrental.watch.watchhistory.dto.WatchHistoryResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/watches/history")
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;

    @PostMapping("/{watchId}")
    public ResponseEntity<WatchHistoryResponseDTO> createHistory(@PathVariable UUID watchId, @RequestBody @Valid WatchHistoryRequestDTO request
    ) {
        return ResponseEntity.ok(
                watchHistoryService.createHistory(watchId, request)
        );
    }

    @GetMapping("/{watchId}")
    public ResponseEntity<List<WatchHistoryResponseDTO>> getHistoryByWatch(
            @PathVariable UUID watchId)
    {
        return ResponseEntity.ok(
                watchHistoryService.getHistoryByWatch(watchId)
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<WatchHistoryResponseDTO> updateHistory(@PathVariable UUID id, @RequestBody @Valid WatchHistoryRequestDTO request)
    {
        return ResponseEntity.ok(
                watchHistoryService.updateHistory(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable UUID id)
    {
        watchHistoryService.deleteHistory(id);

        return ResponseEntity.noContent().build();
    }

}
