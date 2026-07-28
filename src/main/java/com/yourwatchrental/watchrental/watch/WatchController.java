package com.yourwatchrental.watchrental.watch;

import com.yourwatchrental.watchrental.watch.dto.WatchFilterRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.WatchPageResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.WatchRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.WatchResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/watches")
@RequiredArgsConstructor
public class WatchController {
    private final WatchService watchService;

    @PostMapping
    public ResponseEntity<WatchResponseDTO> createWatch(@RequestBody @Valid WatchRequestDTO request)
    {
        WatchResponseDTO watch = watchService.createWatch(request);

        return ResponseEntity.ok(watch);
    }

    @GetMapping
    public ResponseEntity<Page<WatchPageResponseDTO>> getWatchPage(@Valid WatchFilterRequestDTO request, Pageable pageable)
    {
        Page<WatchPageResponseDTO> watches = watchService.getWatchPage(request, pageable);

        return ResponseEntity.ok(watches);
    }
}
