package com.yourwatchrental.watchrental.branch;

import com.yourwatchrental.watchrental.branch.dto.BranchFilterCriteriaRequest;
import com.yourwatchrental.watchrental.branch.dto.BranchRequestDTO;
import com.yourwatchrental.watchrental.branch.dto.BranchResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> getBranchById(@RequestParam UUID id)
    {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }

    @PostMapping
    public ResponseEntity<BranchResponseDTO> createBranch(@RequestBody @Valid BranchRequestDTO request)
    {
        BranchResponseDTO response = branchService.createBranch(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> updateBranch(@PathVariable UUID id, @Valid @RequestBody BranchRequestDTO request)
    {
        BranchResponseDTO response = branchService.updateBranch(id,request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable UUID id)
    {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
}
