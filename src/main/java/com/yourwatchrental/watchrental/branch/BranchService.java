package com.yourwatchrental.watchrental.branch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchService {


    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    public BranchResponseDTO createBranch(BranchRequestDTO request){
        Branch branch = branchMapper.toEntity(request);

        Branch createdBranch = branchRepository.save(branch);

        return branchMapper.toResponseDTO(createdBranch);
    }

    public List<BranchResponseDTO> getBranchesByAddress(String address)
    {
        return branchRepository.findByAddressContainingIgnoreCase(address)
                .stream()
                .map(branchMapper::toResponseDTO)
                .toList();
    }

    public List<BranchResponseDTO> getBranches()
    {
        return branchRepository.findAll()
                .stream()
                .map(branchMapper::toResponseDTO)
                .toList();
    }

    public BranchResponseDTO updateBranch(UUID id, BranchRequestDTO request)
    {
        Branch entity = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));

       branchMapper.updateEntityFromDTO(request, entity);

       Branch updatedBranch = branchRepository.save(entity);

        return branchMapper.toResponseDTO(updatedBranch);
    }

    void deleteBranch(UUID id)
    {
        Branch entity = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));

        branchRepository.delete(entity);
    }
}
