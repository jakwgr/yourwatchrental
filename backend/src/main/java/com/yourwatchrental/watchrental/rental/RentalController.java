package com.yourwatchrental.watchrental.rental;

import com.yourwatchrental.watchrental.rental.dto.request.PaymentStatusChangeRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.request.RentalFilterRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.request.RentalRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.response.RentalResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.request.WatchFilterRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping
    public ResponseEntity<RentalResponseDTO> createRental(@RequestBody @Valid RentalRequestDTO request)
    {
        return ResponseEntity.ok(rentalService.createRental(request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<RentalResponseDTO> cancelRental(@PathVariable UUID id)
    {
        return ResponseEntity.ok(rentalService.cancelRental(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/complete")
    public ResponseEntity<RentalResponseDTO> completeRental(@PathVariable UUID id)
    {
        return ResponseEntity.ok(rentalService.completeRental(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponseDTO> getRentalById(@PathVariable UUID id)
    {
        return ResponseEntity.ok(rentalService.getRentalById(id));
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public Page<RentalResponseDTO> getMyRentals(RentalFilterRequestDTO request, Pageable page)
    {
        return rentalService.getMyRentals(request, page);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<RentalResponseDTO> getAllRentals(RentalFilterRequestDTO request, Pageable page)
    {
        return rentalService.getAllRentals(request, page);
    }

    @PatchMapping("/{id}/payment")
    public ResponseEntity<RentalResponseDTO> changePaymentStatus( @PathVariable UUID id, @RequestBody PaymentStatusChangeRequestDTO request)
    {
        return ResponseEntity
                .ok(rentalService.changePaymentStatus(id, request));
    }
}
