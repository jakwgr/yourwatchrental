package com.yourwatchrental.watchrental.watch;

import com.yourwatchrental.watchrental.rental.RentalService;
import com.yourwatchrental.watchrental.watch.dto.request.*;
import com.yourwatchrental.watchrental.watch.dto.response.WatchAvailabilityResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchCardResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchFullInfoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/watches")
@RequiredArgsConstructor
public class WatchController {
    private final WatchService watchService;
    private final RentalService rentalService;

    @PostMapping
    public ResponseEntity<WatchFullInfoResponseDTO> createWatch(@RequestBody @Valid WatchRequestDTO request) {
        WatchFullInfoResponseDTO watch = watchService.createWatch(request);

        return ResponseEntity.ok(watch);
    }

    @GetMapping
    public ResponseEntity<Page<WatchCardResponseDTO>> getWatchPage(@ModelAttribute WatchFilterRequestDTO request, Pageable pageable) {
        Page<WatchCardResponseDTO> watches = watchService.getWatchPage(request, pageable);

        return ResponseEntity.ok(watches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WatchFullInfoResponseDTO> getWatch(@PathVariable UUID id) {
        return ResponseEntity.ok(watchService.getWatch(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WatchFullInfoResponseDTO> updateWatch(@PathVariable UUID id, @RequestBody @Valid WatchUpdateRequestDTO request)
    {
        return ResponseEntity.ok(watchService.updateWatch(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<WatchFullInfoResponseDTO> updateWatchStatus(@PathVariable UUID id, @RequestBody @Valid WatchStatusUpdateRequestDTO request)
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

    @PatchMapping("/{id}/branch")
    public ResponseEntity<WatchFullInfoResponseDTO> updateWatchBranch(@PathVariable UUID id, @RequestBody @Valid WatchBranchUpdateRequestDTO request)
    {
        return ResponseEntity
                .ok(watchService.updateWatchBranch(id, request));
    }

    @PatchMapping("/{id}/serial_number")
    public ResponseEntity<WatchFullInfoResponseDTO> updateWatchSerialNumber(@PathVariable UUID id, @RequestBody @Valid WatchSerialNumberUpdateRequestDTO request)
    {
        return ResponseEntity
                .ok(watchService.updateWatchSerialNumber(id, request));
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<WatchAvailabilityResponseDTO> watchAvailability(@PathVariable UUID id, @RequestParam LocalDate startDate,
                                                                          @RequestParam LocalDate endDate)
    {
        return ResponseEntity
                .ok(rentalService.watchAvailabilityStatus(id, startDate, endDate));
    }
}
