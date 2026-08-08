package com.yourwatchrental.watchrental.branch;

import com.yourwatchrental.watchrental.branch.dto.BranchFilterCriteriaRequest;
import com.yourwatchrental.watchrental.branch.dto.BranchRequestDTO;
import com.yourwatchrental.watchrental.branch.dto.BranchResponseDTO;
import com.yourwatchrental.watchrental.branch.dto.BranchStatusUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<List<BranchResponseDTO>> getBranches(BranchFilterCriteriaRequest criteria)
    {
        return ResponseEntity.ok(branchService.getBranches(criteria));
    }


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BranchResponseDTO>> getBranchesAdmin(BranchFilterCriteriaRequest criteria)
    {
        return ResponseEntity.ok(branchService.getBranchesAdmin(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> getBranchById(@PathVariable UUID id)
    {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchResponseDTO> createBranch(@RequestBody @Valid BranchRequestDTO request)
    {
        BranchResponseDTO response = branchService.createBranch(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchResponseDTO> updateBranch(@PathVariable UUID id, @Valid @RequestBody BranchRequestDTO request)
    {
        BranchResponseDTO response = branchService.updateBranch(id,request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> hardDeleteBranch(@PathVariable UUID id)
    {
        branchService.hardDeleteBranch(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchResponseDTO> updateBranchStatus(@PathVariable UUID id, @RequestBody @Valid BranchStatusUpdateRequestDTO request)
    {
        return ResponseEntity.ok(branchService.updateBranchStatus(id, request));
    }
}
